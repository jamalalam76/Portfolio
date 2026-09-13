// ===== Theme toggle =====
const root = document.documentElement;
const themeBtn = document.getElementById('themeToggle');
const savedTheme = localStorage.getItem('theme');
if (savedTheme === 'light') root.classList.add('light');
themeBtn.textContent = root.classList.contains('light') ? '☀️' : '🌙';
themeBtn.addEventListener('click', () => {
  root.classList.toggle('light');
  const light = root.classList.contains('light');
  localStorage.setItem('theme', light ? 'light' : 'dark');
  themeBtn.textContent = light ? '☀️' : '🌙';
});

// ===== Mobile menu =====
const menuToggle = document.getElementById('menuToggle');
const nav = document.getElementById('nav');
menuToggle?.addEventListener('click', () => nav.classList.toggle('open'));
nav.querySelectorAll('a').forEach(a => a.addEventListener('click', () => nav.classList.remove('open')));

// ===== Auth state =====
function isLoggedIn() { return !!localStorage.getItem('token'); }
function updateAuthUI() {
  document.getElementById('loginBtn').classList.toggle('hidden', isLoggedIn());
  document.getElementById('logoutBtn').classList.toggle('hidden', !isLoggedIn());
}
document.getElementById('logoutBtn').addEventListener('click', (e) => {
  e.preventDefault();
  localStorage.removeItem('token');
  localStorage.removeItem('username');
  updateAuthUI();
});
updateAuthUI();

// ===== Typing effect =====
const phrases = [
  'B.Tech IT Student',
  'MERN Stack Developer',
  'AI & Web Development Enthusiast',
  'AI Enthusiast 🤖'
];
const typed = document.getElementById('typed');
let pi = 0, ci = 0, deleting = false;
function type() {
  const p = phrases[pi];
  typed.textContent = p.slice(0, ci);
  if (!deleting && ci < p.length) { ci++; setTimeout(type, 80); }
  else if (deleting && ci > 0) { ci--; setTimeout(type, 40); }
  else { deleting = !deleting; if (!deleting) pi = (pi + 1) % phrases.length;
    setTimeout(type, deleting ? 1400 : 300); }
}
type();

// ===== Reveal on scroll =====
const io = new IntersectionObserver((entries) => {
  entries.forEach(e => { if (e.isIntersecting) e.target.classList.add('in-view'); });
}, { threshold: 0.15 });
document.querySelectorAll('.reveal, .skill').forEach(el => io.observe(el));

// ===== Year =====
document.getElementById('year').textContent = new Date().getFullYear();

// ===== Contact form =====
document.getElementById('contactForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const fd = new FormData(e.target);
  const msg = document.getElementById('contactMsg');
  msg.textContent = 'Sending...';
  try {
    const res = await fetch('/api/contact', {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(Object.fromEntries(fd))
    });
    if (!res.ok) throw new Error('Failed');
    msg.textContent = '✓ Message sent! I will reply soon.';
    e.target.reset();
  } catch { msg.textContent = '✗ Could not send. Please try again.'; }
});

// ===== AI chat =====
const aiToggle = document.getElementById('aiToggle');
const aiPanel = document.getElementById('aiPanel');
const aiBody = document.getElementById('aiBody');
const aiForm = document.getElementById('aiForm');
const aiInput = document.getElementById('aiInput');

aiToggle.addEventListener('click', () => aiPanel.classList.toggle('hidden'));
document.getElementById('aiClose').addEventListener('click', () => aiPanel.classList.add('hidden'));

function addMsg(text, cls) {
  const div = document.createElement('div');
  div.className = 'ai-msg ' + cls;
  div.textContent = text;
  aiBody.appendChild(div);
  aiBody.scrollTop = aiBody.scrollHeight;
  return div;
}

aiForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const text = aiInput.value.trim();
  if (!text) return;

  addMsg(text, 'user');
  aiInput.value = '';
  const thinking = addMsg('Thinking...', 'bot thinking');

  try {
    const res = await fetch('/api/ai/chat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ message: text })
    });
    const data = await res.json();
    thinking.remove();
    if (!res.ok) { addMsg('Error: ' + (data.error || res.status), 'bot'); return; }
    addMsg(data.reply || 'No reply.', 'bot');
  } catch (err) {
    thinking.remove();
    addMsg('Network error: ' + err.message, 'bot');
  }
});
