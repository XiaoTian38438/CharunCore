'use strict';

const $ = sel => document.querySelector(sel);
const api = (path, opts) => fetch(path, Object.assign({ credentials: 'same-origin' }, opts));

let ws = null;
let currentPath = '';
let authed = false;
let currentRole = 'admin';
let currentUser = '';

async function boot() {
  const st = await api('/api/status');
  if (st.status === 401) {
    authed = false;
    $('#login-overlay').classList.remove('hidden');
    return;
  }
  authed = true;
  await loadMe();
  initUI();
  connectWs();
  refreshStatus();
  setInterval(refreshStatus, 2000);
  loadFiles('');
}

/* ---------- 登录 ---------- */
$('#login-btn').onclick = doLogin;
$('#login-pwd').addEventListener('keydown', e => { if (e.key === 'Enter') doLogin(); });
async function doLogin() {
  const user = $('#login-user').value;
  const pwd = $('#login-pwd').value;
  const r = await api('/api/login', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ user, password: pwd }) });
  if (r.ok) { $('#login-overlay').classList.add('hidden'); boot(); }
  else { $('#login-err').textContent = '用户名或密码错误'; }
}

async function loadMe() {
  try {
    const r = await api('/api/me');
    const j = await r.json();
    if (j.ok) { currentRole = j.role; currentUser = j.user; }
  } catch (e) {}
  applyRoleUI();
}

function applyRoleUI() {
  const badge = $('#role-badge');
  badge.textContent = '角色: ' + currentRole + (currentUser ? ' (' + currentUser + ')' : '');
  badge.classList.remove('hidden');
  const isAdmin = currentRole === 'admin';
  ['#btn-start', '#btn-stop', '#btn-restart', '#btn-upload'].forEach(sel => {
    const el = $(sel); if (el) el.disabled = !isAdmin;
  });
  if (!isAdmin) {
    $('#btn-upload').title = '需要 admin 角色';
  }
}

/* ---------- 控制按钮 ---------- */
function bind(id, fn) { const el = $(id); if (el) el.onclick = fn; }
bind('#btn-logout', async () => { await api('/api/logout', { method: 'POST' }); location.reload(); });
bind('#btn-start', () => control('start'));
bind('#btn-stop', () => control('stop'));
bind('#btn-restart', () => control('restart'));
bind('#btn-ops', () => showOps());
bind('#ops-close', () => $('#ops-overlay').classList.add('hidden'));

async function control(action) {
  const r = await api('/api/control', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ action }) });
  const j = await r.json().catch(() => ({}));
  termLine('[manager] ' + (j.ok ? ('已执行 ' + action) : ('操作失败: ' + (j.msg || ''))));
  refreshStatus();
}

/* ---------- 状态 ---------- */
async function refreshStatus() {
  const r = await api('/api/status');
  if (r.status === 401) { location.reload(); return; }
  const s = await r.json();
  const dot = $('#status-dot'), txt = $('#status-text'), mem = $('#status-mem');
  if (s.running) {
    dot.className = 'dot on';
    txt.textContent = '25565: 运行中 (pid ' + s.pid + ')';
    mem.textContent = s.memoryMB != null ? '· ' + s.memoryMB + ' MB' : '';
  } else {
    dot.className = 'dot off';
    txt.textContent = '25565: 已停止';
    mem.textContent = s.externalPortOwner ? '(端口被外部 PID ' + s.externalPortOwner + ' 占用)' : '';
  }
  const isAdmin = currentRole === 'admin';
  $('#btn-start').disabled = !isAdmin || s.running;
  $('#btn-stop').disabled = !isAdmin || !s.running;
  $('#btn-restart').disabled = !isAdmin;
}

/* ---------- 终端 ---------- */
function connectWs() {
  const proto = location.protocol === 'https:' ? 'wss' : 'ws';
  ws = new WebSocket(proto + '://' + location.host + '/ws');
  const state = $('#ws-state');
  ws.onopen = () => { state.textContent = '已连接'; state.className = 'ws-state ok'; };
  ws.onclose = () => { state.textContent = '断开，重连中...'; state.className = 'ws-state bad'; setTimeout(connectWs, 2000); };
  ws.onerror = () => { state.textContent = '连接错误'; state.className = 'ws-state bad'; };
  ws.onmessage = ev => termLine(ev.data);
}
function termLine(t) {
  const tEl = $('#terminal');
  const div = document.createElement('div');
  div.className = 'line';
  div.textContent = t;
  tEl.appendChild(div);
  tEl.scrollTop = tEl.scrollHeight;
}
$('#term-input').addEventListener('keydown', e => {
  if (e.key === 'Enter') {
    const v = $('#term-input').value;
    if (v && ws && ws.readyState === 1) { ws.send(v); }
    $('#term-input').value = '';
  }
});

/* ---------- 文件管理 ---------- */
async function loadFiles(rel) {
  currentPath = rel;
  const r = await api('/api/files?path=' + encodeURIComponent(rel));
  if (r.status === 401) { location.reload(); return; }
  const j = await r.json();
  renderBreadcrumb(rel);
  const list = $('#filelist');
  list.innerHTML = '';
  (j.entries || []).forEach(en => {
    const row = document.createElement('div');
    row.className = 'file-row';
    const name = document.createElement('div');
    name.className = 'name' + (en.isDir ? ' dir' : '');
    name.textContent = en.name + (en.isDir ? '/' : '');
    if (en.isDir) name.onclick = () => loadFiles(en.rel);
    const meta = document.createElement('div');
    meta.className = 'meta';
    meta.textContent = en.isDir ? '' : fmtSize(en.size);
    const acts = document.createElement('div');
    acts.className = 'acts';
    if (!en.isDir) {
      const open = mkBtn('打开', () => openFile(en.rel));
      const dl = mkBtn('下载', () => downloadFile(en.rel));
      acts.append(open, dl);
      if (currentRole === 'admin') acts.append(mkBtn('编辑', () => editFile(en.rel)));
    }
    if (currentRole === 'admin') acts.append(mkBtn('删除', () => delFile(en.rel, en.isDir), 'del'));
    row.append(name, meta, acts);
    list.appendChild(row);
  });
}
function mkBtn(label, fn, cls) {
  const b = document.createElement('button');
  b.textContent = label; if (cls) b.className = cls;
  b.onclick = (e) => { e.stopPropagation(); fn(); };
  return b;
}
function renderBreadcrumb(rel) {
  const bc = $('#breadcrumb');
  bc.innerHTML = '';
  const parts = rel ? rel.split('/') : [];
  const root = document.createElement('span');
  root.className = 'crumb'; root.textContent = '根';
  root.onclick = () => loadFiles('');
  bc.appendChild(root);
  let acc = '';
  parts.forEach(p => {
    acc = acc ? acc + '/' + p : p;
    const sep = document.createElement('span'); sep.textContent = ' / '; bc.appendChild(sep);
    const c = document.createElement('span'); c.className = 'crumb'; c.textContent = p;
    c.onclick = () => loadFiles(acc); bc.appendChild(c);
  });
}
function fmtSize(n) {
  if (n < 1024) return n + ' B';
  if (n < 1048576) return (n / 1024).toFixed(1) + ' KB';
  return (n / 1048576).toFixed(1) + ' MB';
}

async function openFile(rel) {
  const r = await api('/api/file/content?path=' + encodeURIComponent(rel));
  const j = await r.json();
  if (j.binary) { downloadFile(rel); return; }
  editFile(rel, j.content, j.size);
}

async function editFile(rel, prefill, size) {
  if (prefill === undefined) {
    const r = await api('/api/file/content?path=' + encodeURIComponent(rel));
    const j = await r.json();
    if (j.binary) { alert('该文件为二进制，无法编辑，可下载。'); return; }
    prefill = j.content; size = j.size;
  }
  $('#edit-title').textContent = '编辑: ' + rel;
  $('#edit-area').value = prefill || '';
  $('#edit-info').textContent = size != null ? fmtSize(size) : '';
  $('#edit-overlay').dataset.rel = rel;
  $('#edit-overlay').classList.remove('hidden');
}
$('#edit-close').onclick = () => $('#edit-overlay').classList.add('hidden');
$('#edit-save').onclick = async () => {
  const rel = $('#edit-overlay').dataset.rel;
  const content = $('#edit-area').value;
  const r = await api('/api/file/content', { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ path: rel, content }) });
  const j = await r.json().catch(() => ({}));
  if (j.ok) { $('#edit-overlay').classList.add('hidden'); termLine('[manager] 已保存 ' + rel); loadFiles(currentPath); }
  else alert('保存失败: ' + (j.msg || ''));
};

function downloadFile(rel) {
  window.open('/api/file/download?path=' + encodeURIComponent(rel), '_blank');
}

async function delFile(rel, isDir) {
  if (!confirm('确认删除 ' + (isDir ? '目录(含子项) ' : '') + rel + ' ?')) return;
  const r = await api('/api/file?path=' + encodeURIComponent(rel), { method: 'DELETE' });
  const j = await r.json().catch(() => ({}));
  if (j.ok) { termLine('[manager] 已删除 ' + rel); loadFiles(currentPath); }
  else alert('删除失败: ' + (j.msg || ''));
}

$('#upload-input').onchange = async (e) => {
  const f = e.target.files[0];
  if (!f) return;
  const buf = await f.arrayBuffer();
  const data = btoa(String.fromCharCode(...new Uint8Array(buf)));
  const r = await api('/api/file/upload', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ path: currentPath, filename: f.name, data }) });
  const j = await r.json().catch(() => ({}));
  if (j.ok) { termLine('[manager] 已上传 ' + j.name); loadFiles(currentPath); }
  else alert('上传失败: ' + (j.msg || ''));
  e.target.value = '';
};
$('#btn-upload').onclick = () => {
  if (!currentPath) { alert('请先选择一个 players 或 world 下的目录'); return; }
  $('#upload-input').click();
};

/* ---------- 根目录切换 ---------- */
document.querySelectorAll('.root-btn').forEach(b => {
  b.onclick = () => {
    document.querySelectorAll('.root-btn').forEach(x => x.classList.remove('active'));
    b.classList.add('active');
    loadFiles(b.dataset.root);
  };
});

/* ---------- 操作日志 ---------- */
async function showOps() {
  const r = await api('/api/ops');
  const j = await r.json().catch(() => ({ ops: [] }));
  const box = $('#ops-list');
  box.innerHTML = '';
  (j.ops || []).forEach(o => {
    const d = document.createElement('div');
    d.className = 'op';
    d.textContent = '[' + o.ts + '] [' + o.ip + '] ' + o.action + ' ' + o.detail;
    box.appendChild(d);
  });
  $('#ops-overlay').classList.remove('hidden');
}

function initUI() { /* 绑定已在全局完成 */ }

boot();
