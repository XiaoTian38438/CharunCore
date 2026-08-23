'use strict';

const http = require('http');
const fs = require('fs');
const path = require('path');
const crypto = require('crypto');
const { spawn, execFileSync } = require('child_process');

const __managerDir = __dirname;
const CONFIG = loadConfig();
const PORT = CONFIG.port || 25566;
const serverRoot = path.resolve(__managerDir, CONFIG.serverRoot || '..');
const PUBLIC_DIR = path.join(__managerDir, 'public');
const PID_FILE = path.join(__managerDir, '25566.pid');
const OP_LOG_FILE = path.join(__managerDir, 'ops.log');

const ALLOWED_BASES = ['players', 'world'];

const sessions = new Map(); // sid -> { user, role }
const wsClients = new Set();
const ringBuffer = [];
const RING_MAX = 1000;
let terminalPartial = '';
let child = null;
let childStartTime = 0;
let memCache = { pid: null, mb: null, t: 0 };
let managerStart = Date.now();

const SESSION_MAX_AGE = 24 * 3600;

function loadConfig() {
  const p = path.join(__managerDir, 'config.json');
  let cfg = {};
  try { cfg = JSON.parse(fs.readFileSync(p, 'utf8')); } catch (e) { cfg = {}; }
  const auth = Object.assign({ enabled: true, password: 'admin', users: [] }, cfg.auth || {});
  return Object.assign({
    port: 25566,
    serverRoot: '..',
    javaCmd: '',
    autoStartServer: true,
    maxFileEditSize: 5 * 1024 * 1024,
    maxUploadSize: 20 * 1024 * 1024,
    opLogSize: 500
  }, cfg, { auth });
}

function sha256(s) { return crypto.createHash('sha256').update(String(s)).digest('hex'); }

function appendOp(action, detail, req) {
  const ts = new Date().toISOString();
  const ip = req && req.socket ? (req.socket.remoteAddress || '') : '';
  const entry = { ts, ip, action, detail: detail || '' };
  try { fs.appendFileSync(OP_LOG_FILE, JSON.stringify(entry) + '\n'); } catch (e) {}
}

function readOps(limit) {
  try {
    const lines = fs.readFileSync(OP_LOG_FILE, 'utf8').split('\n').filter(Boolean);
    const all = lines.map(l => { try { return JSON.parse(l); } catch (e) { return null; } }).filter(Boolean);
    return all.slice(-(limit || CONFIG.opLogSize)).reverse();
  } catch (e) { return []; }
}

function resolveJava() {
  if (CONFIG.javaCmd && CONFIG.javaCmd.trim()) return CONFIG.javaCmd.trim();
  const jh = process.env.JAVA_HOME;
  if (jh) {
    const p = path.join(jh, 'bin', 'java');
    if (fs.existsSync(p)) return p;
  }
  return 'java';
}

function getRuncp() {
  try { return fs.readFileSync(path.join(serverRoot, 'runcp.txt'), 'utf8').trim(); }
  catch (e) { return ''; }
}

function broadcastLine(line) {
  ringBuffer.push(line);
  if (ringBuffer.length > RING_MAX) ringBuffer.shift();
  for (const c of wsClients) sendWs(c, line);
}

function pushOutput(chunk) {
  const text = chunk.toString('utf8');
  terminalPartial += text;
  let idx;
  while ((idx = terminalPartial.indexOf('\n')) >= 0) {
    let line = terminalPartial.slice(0, idx);
    if (line.endsWith('\r')) line = line.slice(0, -1);
    terminalPartial = terminalPartial.slice(idx + 1);
    broadcastLine(line);
  }
}

function getPortInfo() {
  try {
    const out = execFileSync('cmd', ['/c', 'netstat -ano 2>nul | findstr :25565 | findstr LISTENING'], { windowsHide: true }).toString();
    const lines = out.split('\n').map(l => l.trim()).filter(l => l.includes('LISTENING'));
    if (lines.length) {
      const m = lines[0].split(/\s+/);
      const pid = parseInt(m[m.length - 1], 10);
      return { listening: true, pid: isNaN(pid) ? null : pid };
    }
  } catch (e) {}
  return { listening: false, pid: null };
}

function getMemMB(pid) {
  const now = Date.now();
  if (memCache.pid === pid && now - memCache.t < 2000) return memCache.mb;
  let mb = null;
  try {
    const out = execFileSync('powershell', ['-NoProfile', '-Command', '(Get-Process -Id ' + pid + ' -ErrorAction SilentlyContinue).WorkingSet / 1MB'], { windowsHide: true }).toString();
    const v = parseFloat(out);
    if (!isNaN(v)) mb = Math.round(v);
  } catch (e) {}
  memCache = { pid, mb, t: now };
  return mb;
}

function statusSnapshot() {
  const port = getPortInfo();
  let memoryMB = null, uptime = 0, pid = null;
  if (child && !child.killed) {
    pid = child.pid;
    memoryMB = getMemMB(pid);
    uptime = Math.floor((Date.now() - childStartTime) / 1000);
  }
  return {
    running: !!(child && !child.killed),
    pid,
    uptime,
    memoryMB,
    port25565Listening: port.listening,
    externalPortOwner: (port.listening && (!child || child.pid !== port.pid)) ? port.pid : null,
    managerUptime: Math.floor((Date.now() - managerStart) / 1000),
    javaCmd: resolveJava(),
    serverRoot,
    authEnabled: !!CONFIG.auth.enabled,
    https: false
  };
}

function startServer() {
  if (child && !child.killed) return { ok: false, msg: '25565 already running (pid ' + child.pid + ')' };
  const port = getPortInfo();
  if (port.listening && (!child || child.pid !== port.pid)) {
    return { ok: false, msg: 'Port 25565 is occupied by external PID ' + port.pid + '. Stop it first.' };
  }
  const java = resolveJava();
  const cp = getRuncp();
  if (!cp) return { ok: false, msg: 'Cannot read runcp.txt, no classpath found.' };
  const args = ['-cp', cp, 'com.yanrong.server.Main'];
  try {
    child = spawn(java, args, { cwd: serverRoot, stdio: ['pipe', 'pipe', 'pipe'] });
  } catch (e) {
    return { ok: false, msg: 'start failed: ' + e.message };
  }
  childStartTime = Date.now();
  child.stdout.on('data', d => pushOutput(d));
  child.stderr.on('data', d => pushOutput(d));
  child.on('error', e => broadcastLine('[manager] start error: ' + e.message));
  child.on('exit', code => {
    broadcastLine('[manager] 25565 exited (code ' + code + ')');
    child = null;
    memCache = { pid: null, mb: null, t: 0 };
  });
  broadcastLine('[manager] 25565 starting (pid ' + child.pid + ')...');
  appendOp('START', 'pid=' + child.pid, null);
  return { ok: true, pid: child.pid };
}

function stopServer(force) {
  if (!child || child.killed) return { ok: false, msg: '25565 not running' };
  const pid = child.pid;
  appendOp('STOP', 'pid=' + pid, null);
  try { child.stdin.write('stop\n'); } catch (e) {}
  if (force) {
    try { child.kill('SIGKILL'); } catch (e) {}
    broadcastLine('[manager] forced stop 25565 (pid ' + pid + ')');
    return { ok: true };
  }
  broadcastLine('[manager] stop command sent, waiting graceful shutdown...');
  setTimeout(() => {
    if (child && child.pid === pid && !child.killed) {
      try { child.kill('SIGKILL'); } catch (e) {}
      broadcastLine('[manager] 25565 did not exit in 5s, forced stop (pid ' + pid + ')');
    }
  }, 5000);
  return { ok: true };
}

function restartServer() {
  if (child && !child.killed) {
    stopServer(false);
    setTimeout(() => {
      const r = startServer();
      if (!r.ok) broadcastLine('[manager] restart failed: ' + r.msg);
    }, 1500);
    return { ok: true, msg: 'restarting...' };
  }
  const r = startServer();
  return r;
}

function safePath(rel) {
  if (typeof rel !== 'string') return null;
  const clean = rel.replace(/\\/g, '/');
  const parts = clean.split('/').filter(Boolean);
  if (parts.length === 0) return { abs: null, base: null, rel: '' };
  const base = parts[0];
  if (ALLOWED_BASES.indexOf(base) < 0) return null;
  const abs = path.resolve(serverRoot, parts.join(path.sep));
  const allowed = path.resolve(serverRoot, base);
  if (abs !== allowed && !abs.startsWith(allowed + path.sep)) return null;
  return { abs, base, rel: parts.join('/') };
}

const MIME = {
  '.html': 'text/html; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.gif': 'image/gif',
  '.ico': 'image/x-icon',
  '.svg': 'image/svg+xml',
  '.txt': 'text/plain; charset=utf-8'
};

function sendJson(res, code, obj) {
  const b = Buffer.from(JSON.stringify(obj), 'utf8');
  res.writeHead(code, { 'Content-Type': 'application/json; charset=utf-8', 'Content-Length': b.length });
  res.end(b);
}

function parseCookies(req) {
  const h = req.headers.cookie;
  const out = {};
  if (!h) return out;
  h.split(';').forEach(c => {
    const i = c.indexOf('=');
    if (i >= 0) out[c.slice(0, i).trim()] = decodeURIComponent(c.slice(i + 1).trim());
  });
  return out;
}

function getSession(req) {
  const c = parseCookies(req);
  if (!c.ysid) return null;
  const s = sessions.get(c.ysid);
  return s || null;
}
function validSession(req) { return !!getSession(req); }
function roleOf(req) { const s = getSession(req); return s ? s.role : null; }

function setSessionCookie(res, user, role) {
  const sid = crypto.randomBytes(24).toString('hex');
  sessions.set(sid, { user: user || 'anonymous', role: role || 'viewer' });
  res.setHeader('Set-Cookie', 'ysid=' + sid + '; HttpOnly; Path=/; Max-Age=' + SESSION_MAX_AGE + '; SameSite=Lax');
}

function clearSessionCookie(res) {
  res.setHeader('Set-Cookie', 'ysid=; HttpOnly; Path=/; Max-Age=0; SameSite=Lax');
}

function readBody(req, limit) {
  limit = limit || (1 << 20);
  return new Promise((resolve, reject) => {
    let data = ''; let size = 0;
    req.on('data', c => {
      size += c.length;
      if (size > limit) { reject(new Error('body too large')); req.destroy(); return; }
      data += c;
    });
    req.on('end', () => { try { resolve(data ? JSON.parse(data) : {}); } catch (e) { reject(e); } });
    req.on('error', reject);
  });
}

function serveStatic(res, relPath) {
  let fp = path.join(PUBLIC_DIR, relPath);
  fp = path.resolve(PUBLIC_DIR, relPath);
  if (!fp.startsWith(PUBLIC_DIR)) { res.writeHead(403); res.end('forbidden'); return; }
  if (!fs.existsSync(fp) || !fs.statSync(fp).isFile()) { res.writeHead(404); res.end('not found'); return; }
  const ext = path.extname(fp).toLowerCase();
  const ct = MIME[ext] || 'application/octet-stream';
  res.writeHead(200, { 'Content-Type': ct });
  fs.createReadStream(fp).pipe(res);
}

function requireAdmin(req, res) {
  if (roleOf(req) !== 'admin') {
    sendJson(res, 403, { ok: false, msg: '权限不足 (需要 admin 角色)' });
    return false;
  }
  return true;
}

function handleApi(req, res, u, pathname) {
  if (pathname === '/api/login' && req.method === 'POST') {
    readBody(req).then(body => {
      if (!CONFIG.auth.enabled) { setSessionCookie(res, 'anonymous', 'admin'); sendJson(res, 200, { ok: true, role: 'admin' }); return; }
      const users = CONFIG.auth.users || [];
      const user = body && body.user;
      const pass = body && body.password;
      let ok = false, role = 'viewer', uname = 'anonymous';
      if (users.length) {
        const found = users.find(x => x.user === user && sha256(pass || '') === sha256(x.pass || ''));
        if (found) { ok = true; role = found.role || 'viewer'; uname = found.user; }
      } else if (sha256(pass || '') === sha256(CONFIG.auth.password || '')) {
        ok = true; role = 'admin'; uname = 'admin';
      }
      if (ok) { setSessionCookie(res, uname, role); appendOp('LOGIN', 'success user=' + uname, req); sendJson(res, 200, { ok: true, role }); }
      else { appendOp('LOGIN', 'fail user=' + (user || ''), req); sendJson(res, 401, { ok: false, msg: '用户名或密码错误' }); }
    }).catch(() => sendJson(res, 400, { ok: false, msg: 'bad request' }));
    return;
  }
  if (pathname === '/api/me' && req.method === 'GET') {
    if (!validSession(req)) { sendJson(res, 401, { ok: false }); return; }
    const s = getSession(req);
    sendJson(res, 200, { ok: true, user: s.user, role: s.role });
    return;
  }
  if (pathname === '/api/logout' && req.method === 'POST') {
    const c = parseCookies(req);
    if (c.ysid) sessions.delete(c.ysid);
    clearSessionCookie(res);
    sendJson(res, 200, { ok: true });
    return;
  }
  if (!validSession(req)) { sendJson(res, 401, { ok: false, msg: '未登录' }); return; }

  if (pathname === '/api/status' && req.method === 'GET') {
    sendJson(res, 200, statusSnapshot());
    return;
  }
  if (pathname === '/api/control' && req.method === 'POST') {
    if (!requireAdmin(req, res)) return;
    readBody(req).then(body => {
      const a = (body && body.action) || '';
      let r;
      if (a === 'start') r = startServer();
      else if (a === 'stop') r = stopServer(false);
      else if (a === 'restart') r = restartServer();
      else if (a === 'force-stop') r = stopServer(true);
      else r = { ok: false, msg: 'unknown action' };
      sendJson(res, r.ok ? 200 : 400, r);
    }).catch(() => sendJson(res, 400, { ok: false, msg: 'bad request' }));
    return;
  }
  if (pathname === '/api/ops' && req.method === 'GET') {
    sendJson(res, 200, { ops: readOps(CONFIG.opLogSize) });
    return;
  }
  if (pathname === '/api/files' && req.method === 'GET') {
    const rel = u.searchParams.get('path') || '';
    const sp = safePath(rel);
    if (!sp) { sendJson(res, 403, { ok: false, msg: '越权路径' }); return; }
    if (rel === '' || rel === '/' || rel === '.') {
      const roots = ALLOWED_BASES.filter(b => fs.existsSync(path.join(serverRoot, b)))
        .map(b => ({ name: b, rel: b, isDir: true, size: 0, mtime: 0 }));
      sendJson(res, 200, { path: '', entries: roots });
      return;
    }
    try {
      const st = fs.statSync(sp.abs);
      if (st.isDirectory()) {
        const entries = fs.readdirSync(sp.abs).map(n => {
          const fp = path.join(sp.abs, n);
          let s; try { s = fs.statSync(fp); } catch (e) { s = { isDirectory: () => false, size: 0, mtime: 0 }; }
          return {
            name: n,
            rel: sp.rel + '/' + n,
            isDir: s.isDirectory(),
            size: s.size,
            mtime: s.mtime ? s.mtime.getTime() : 0
          };
        }).sort((a, b) => (b.isDir - a.isDir) || a.name.localeCompare(b.name));
        sendJson(res, 200, { path: sp.rel, entries });
      } else {
        sendJson(res, 200, {
          path: sp.rel,
          entries: [{ name: path.basename(sp.abs), rel: sp.rel, isDir: false, size: st.size, mtime: st.mtime ? st.mtime.getTime() : 0 }]
        });
      }
    } catch (e) { sendJson(res, 404, { ok: false, msg: '路径不存在' }); }
    return;
  }
  if (pathname === '/api/file/content' && req.method === 'GET') {
    const rel = u.searchParams.get('path') || '';
    const sp = safePath(rel);
    if (!sp) { sendJson(res, 403, { ok: false, msg: '越权路径' }); return; }
    try {
      const st = fs.statSync(sp.abs);
      if (st.isDirectory()) { sendJson(res, 400, { ok: false, msg: '是目录' }); return; }
      if (st.size > CONFIG.maxFileEditSize) { sendJson(res, 413, { ok: false, msg: '文件过大，无法编辑' }); return; }
      const buf = fs.readFileSync(sp.abs);
      const head = buf.slice(0, 512);
      const binary = head.includes(0x00);
      if (binary) { sendJson(res, 200, { path: sp.rel, binary: true, size: st.size }); return; }
      sendJson(res, 200, { path: sp.rel, binary: false, size: st.size, content: buf.toString('utf8') });
    } catch (e) { sendJson(res, 404, { ok: false, msg: '文件不存在' }); }
    return;
  }
  if (pathname === '/api/file/content' && req.method === 'PUT') {
    if (!requireAdmin(req, res)) return;
    readBody(req, CONFIG.maxFileEditSize + (1 << 20)).then(body => {
      const rel = body.path;
      const sp = safePath(rel);
      if (!sp) { sendJson(res, 403, { ok: false, msg: '越权路径' }); return; }
      try {
        const st = fs.statSync(sp.abs);
        if (st.isDirectory()) { sendJson(res, 400, { ok: false, msg: '是目录' }); return; }
      } catch (e) { /* new */ }
      fs.writeFileSync(sp.abs, body.content != null ? String(body.content) : '', 'utf8');
      appendOp('EDIT', rel, req);
      sendJson(res, 200, { ok: true });
    }).catch(() => sendJson(res, 400, { ok: false, msg: 'bad request' }));
    return;
  }
  if (pathname === '/api/file' && req.method === 'DELETE') {
    if (!requireAdmin(req, res)) return;
    const rel = u.searchParams.get('path') || '';
    const sp = safePath(rel);
    if (!sp) { sendJson(res, 403, { ok: false, msg: '越权路径' }); return; }
    try {
      const st = fs.statSync(sp.abs);
      if (st.isDirectory()) {
        fs.rmSync(sp.abs, { recursive: true, force: true });
      } else {
        fs.unlinkSync(sp.abs);
      }
      appendOp('DELETE', rel, req);
      sendJson(res, 200, { ok: true });
    } catch (e) { sendJson(res, 400, { ok: false, msg: '删除失败: ' + e.message }); }
    return;
  }
  if (pathname === '/api/file/download' && req.method === 'GET') {
    const rel = u.searchParams.get('path') || '';
    const sp = safePath(rel);
    if (!sp) { res.writeHead(403); res.end('forbidden'); return; }
    try {
      const st = fs.statSync(sp.abs);
      if (st.isDirectory()) { res.writeHead(400); res.end('is dir'); return; }
      res.writeHead(200, {
        'Content-Type': 'application/octet-stream',
        'Content-Disposition': 'attachment; filename="' + encodeURIComponent(path.basename(sp.abs)) + '"',
        'Content-Length': st.size
      });
      fs.createReadStream(sp.abs).pipe(res);
      appendOp('DOWNLOAD', rel, req);
    } catch (e) { res.writeHead(404); res.end('not found'); }
    return;
  }
  if (pathname === '/api/file/upload' && req.method === 'POST') {
    if (!requireAdmin(req, res)) return;
    readBody(req, CONFIG.maxUploadSize + (1 << 20)).then(body => {
      const dir = safePath(body.path || '');
      if (!dir || !fs.existsSync(dir.abs) || !fs.statSync(dir.abs).isDirectory()) {
        sendJson(res, 403, { ok: false, msg: '目标目录越权或不存在' }); return;
      }
      if (!body.filename) { sendJson(res, 400, { ok: false, msg: '缺少文件名' }); return; }
      const safeName = path.basename(body.filename).replace(/[\\/:*?"<>|]/g, '_');
      const buf = Buffer.from(body.data || '', 'base64');
      if (buf.length > CONFIG.maxUploadSize) { sendJson(res, 413, { ok: false, msg: '文件过大' }); return; }
      fs.writeFileSync(path.join(dir.abs, safeName), buf);
      appendOp('UPLOAD', (body.path || '') + '/' + safeName, req);
      sendJson(res, 200, { ok: true, name: safeName });
    }).catch(() => sendJson(res, 400, { ok: false, msg: 'bad request' }));
    return;
  }
  sendJson(res, 404, { ok: false, msg: 'no such api' });
}

function makeHandler() {
  return (req, res) => {
    const u = new URL(req.url, 'http://localhost');
    const pathname = decodeURIComponent(u.pathname);
    if (pathname.startsWith('/api/')) { handleApi(req, res, u, pathname); return; }
    if (pathname === '/' || pathname === '') { serveStatic(res, 'index.html'); return; }
    serveStatic(res, pathname.replace(/^\//, ''));
  };
}

function setupWsUpgrade(server) {
  server.on('upgrade', (req, socket) => {
    const u = new URL(req.url, 'http://localhost');
    if (u.pathname !== '/ws') { socket.destroy(); return; }
    if (!validSession(req)) { socket.destroy(); return; }
    const key = req.headers['sec-websocket-key'];
    if (!key) { socket.destroy(); return; }
    const accept = crypto.createHash('sha1').update(key + '258EAFA5-E914-47DA-95CA-C5AB0DC85B11').digest('base64');
    socket.write(
      'HTTP/1.1 101 Switching Protocols\r\n' +
      'Upgrade: websocket\r\n' +
      'Connection: Upgrade\r\n' +
      'Sec-WebSocket-Accept: ' + accept + '\r\n\r\n'
    );
    setupWs(socket);
  });
}

function sendWsRaw(socket, opcode, payload) {
  const len = payload.length;
  let header;
  if (len < 126) { header = Buffer.alloc(2); header[0] = 0x80 | opcode; header[1] = len; }
  else if (len < 65536) { header = Buffer.alloc(4); header[0] = 0x80 | opcode; header[1] = 126; header.writeUInt16BE(len, 2); }
  else { header = Buffer.alloc(10); header[0] = 0x80 | opcode; header[1] = 127; header.writeUInt32BE(0, 2); header.writeUInt32BE(len, 6); }
  try { socket.write(Buffer.concat([header, payload])); } catch (e) {}
}

function sendWs(socket, str) { sendWsRaw(socket, 0x1, Buffer.from(str, 'utf8')); }

function setupWs(socket) {
  let buf = Buffer.alloc(0);
  socket.on('data', chunk => {
    buf = Buffer.concat([buf, chunk]);
    while (true) {
      if (buf.length < 2) break;
      const b0 = buf[0], b1 = buf[1];
      const opcode = b0 & 0x0f;
      const masked = (b1 & 0x80) !== 0;
      let len = b1 & 0x7f;
      let offset = 2;
      if (len === 126) { if (buf.length < 4) break; len = buf.readUInt16BE(2); offset = 4; }
      else if (len === 127) { if (buf.length < 10) break; const hi = buf.readUInt32BE(2), lo = buf.readUInt32BE(6); len = hi * 4294967296 + lo; offset = 10; }
      let maskKey;
      if (masked) { if (buf.length < offset + 4) break; maskKey = buf.slice(offset, offset + 4); offset += 4; }
      if (buf.length < offset + len) break;
      let payload = buf.slice(offset, offset + len);
      if (masked) { const out = Buffer.alloc(len); for (let i = 0; i < len; i++) out[i] = payload[i] ^ maskKey[i & 3]; payload = out; }
      buf = buf.slice(offset + len);
      handleWsFrame(socket, opcode, payload);
    }
  });
  socket.on('close', () => wsClients.delete(socket));
  socket.on('error', () => wsClients.delete(socket));
  wsClients.add(socket);
  for (const line of ringBuffer) sendWs(socket, line);
  sendWs(socket, '[manager] 终端已连接。25565 当前状态: ' + (child ? ('运行中 pid ' + child.pid) : '已停止'));
}

function handleWsFrame(socket, opcode, payload) {
  if (opcode === 0x1) {
    const text = payload.toString('utf8');
    if (child && child.stdin && !child.killed) {
      // 回显指令到所有终端客户端，使 25566 屏幕可见用户发送了什么（25565 管道 stdin 不会回显）
      broadcastLine('> ' + text);
      try { child.stdin.write(text + '\n'); } catch (e) { sendWs(socket, '[manager] 写入 stdin 失败: ' + e.message); }
    } else {
      sendWs(socket, '[manager] 25565 未运行，指令未发送。请先点击「启动」。');
    }
  } else if (opcode === 0x8) {
    socket.end();
  } else if (opcode === 0x9) {
    sendWsRaw(socket, 0xA, payload);
  }
}

managerStart = Date.now();

const handler = makeHandler();
const server = http.createServer(handler);
setupWsUpgrade(server);

server.listen(PORT, () => {
  try { fs.writeFileSync(PID_FILE, String(process.pid)); } catch (e) {}
  const proto = 'http';
  console.log('[manager] Manager panel started: ' + proto + '://localhost:' + PORT);
  console.log('[manager] Server root: ' + serverRoot);
  console.log('[manager] Auth: ' + (CONFIG.auth.enabled ? ('enabled (' + ((CONFIG.auth.users || []).length ? ((CONFIG.auth.users.length) + ' users') : 'single password') + ')') : 'disabled'));
  if (CONFIG.autoStartServer) {
    const r = startServer();
    if (!r.ok) console.log('[manager] Auto-start 25565 failed: ' + r.msg);
  }
});

function shutdown() {
  try { if (fs.existsSync(PID_FILE)) fs.unlinkSync(PID_FILE); } catch (e) {}
  if (child && !child.killed) { try { child.kill('SIGKILL'); } catch (e) {} }
  process.exit(0);
}
process.on('SIGINT', shutdown);
process.on('SIGTERM', shutdown);
process.on('exit', () => { try { if (fs.existsSync(PID_FILE)) fs.unlinkSync(PID_FILE); } catch (e) {} });
