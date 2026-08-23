'use strict';
const net = require('net');
const crypto = require('crypto');
const http = require('http');

const HOST = 'localhost';
const PORT = 25566;

function httpReq(method, path, body, headers) {
  return new Promise((resolve, reject) => {
    const data = body ? Buffer.from(body) : null;
    const h = Object.assign({ 'Host': HOST + ':' + PORT, 'Content-Type': 'application/json' }, headers || {});
    if (data) h['Content-Length'] = data.length;
    const req = http.request({ host: HOST, port: PORT, method, path, headers: h }, res => {
      let buf = '';
      res.on('data', c => buf += c);
      res.on('end', () => resolve({ status: res.statusCode, headers: res.headers, body: buf }));
    });
    req.on('error', reject);
    if (data) req.write(data);
    req.end();
  });
}

function maskFrame(text) {
  const payload = Buffer.from(text, 'utf8');
  const mask = crypto.randomBytes(4);
  const len = payload.length;
  let header;
  if (len < 126) { header = Buffer.alloc(2); header[0] = 0x81; header[1] = 0x80 | len; }
  else if (len < 65536) { header = Buffer.alloc(4); header[0] = 0x81; header[1] = 0x80 | 126; header.writeUInt16BE(len, 2); }
  else { header = Buffer.alloc(10); header[0] = 0x81; header[1] = 0x80 | 127; header.writeUInt32BE(0, 2); header.writeUInt32BE(len, 6); }
  const masked = Buffer.alloc(len);
  for (let i = 0; i < len; i++) masked[i] = payload[i] ^ mask[i & 3];
  return Buffer.concat([header, mask, masked]);
}

function wsConnect(cookie) {
  return new Promise((resolve, reject) => {
    const received = [];
    let partial = Buffer.alloc(0);
    const sock = net.connect(PORT, HOST, () => {
      const key = crypto.randomBytes(16).toString('base64');
      const req = [
        'GET /ws HTTP/1.1', 'Host: ' + HOST + ':' + PORT,
        'Upgrade: websocket', 'Connection: Upgrade',
        'Sec-WebSocket-Key: ' + key, 'Sec-WebSocket-Version: 13',
        'Cookie: ' + cookie, '', ''
      ].join('\r\n');
      sock.write(req);
    });
    let handshook = false;
    sock.on('data', chunk => {
      if (!handshook) {
        let buf = Buffer.concat([partial, chunk]);
        const idx = buf.indexOf('\r\n\r\n');
        if (idx < 0) { partial = buf; return; }
        const headerText = buf.slice(0, idx).toString();
        if (!/101/.test(headerText)) { reject(new Error('handshake failed:\n' + headerText)); return; }
        handshook = true;
        const rest = buf.slice(idx + 4);
        parseFrames(rest, received);
        resolve({
          send: t => sock.write(maskFrame(t)),
          received
        });
        return;
      }
      parseFrames(chunk, received);
    });
    sock.on('error', reject);
  });
}

function parseFrames(chunk, received) {
  parseFrames.buf = Buffer.concat([parseFrames.buf || Buffer.alloc(0), chunk]);
  let buf = parseFrames.buf;
  while (buf.length >= 2) {
    const b1 = buf[1];
    let len = b1 & 0x7f;
    let offset = 2;
    if (len === 126) { if (buf.length < 4) break; len = buf.readUInt16BE(2); offset = 4; }
    else if (len === 127) { if (buf.length < 10) break; len = buf.readUInt32BE(6); offset = 10; }
    if (buf.length < offset + len) break;
    const payload = buf.slice(offset, offset + len);
    received.push(payload.toString('utf8'));
    buf = buf.slice(offset + len);
  }
  parseFrames.buf = buf;
}

(async () => {
  const login = await httpReq('POST', '/api/login', JSON.stringify({ user: 'admin', password: 'admin' }));
  const setCookie = login.headers['set-cookie'];
  if (!setCookie) { console.log('NO SET-COOKIE. login=', login.status, login.body); process.exit(1); }
  const m = /ysid=([^;]+)/.exec(setCookie);
  const cookie = 'ysid=' + (m ? m[1] : '');
  console.log('login status=' + login.status);

  const ws = await wsConnect(cookie);
  console.log('WS connected. sending "help"...');
  ws.send('help');
  await new Promise(r => setTimeout(r, 3500));
  console.log('=== received ' + ws.received.length + ' lines ===');
  for (const l of ws.received) console.log('  | ' + l);
  const hasEcho = ws.received.some(l => l === '> help');
  console.log('=== ECHO FIX ' + (hasEcho ? 'PASS (saw "> help")' : 'FAIL (no "> help" echo)') + ' ===');
  process.exit(hasEcho ? 0 : 2);
})().catch(e => { console.error('ERR', e); process.exit(3); });
