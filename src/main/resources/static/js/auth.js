async function post(url, body) {
  const res = await fetch(url, {
    method: 'POST', headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body)
  });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.error || ('HTTP ' + res.status));
  return data;
}

const loginForm = document.getElementById('loginForm');
if (loginForm) {
  loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const msg = document.getElementById('msg');
    msg.textContent = 'Signing in...';
    try {
      const fd = Object.fromEntries(new FormData(loginForm));
      const data = await post('/api/auth/login', fd);
      localStorage.setItem('token', data.token);
      localStorage.setItem('username', data.username);
      msg.textContent = '✓ Welcome, ' + data.username;
      setTimeout(() => location.href = '/', 600);
    } catch (err) { msg.textContent = '✗ ' + err.message; }
  });
}

const regForm = document.getElementById('registerForm');
if (regForm) {
  regForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const msg = document.getElementById('msg');
    msg.textContent = 'Creating account...';
    try {
      const fd = Object.fromEntries(new FormData(regForm));
      const data = await post('/api/auth/register', fd);
      localStorage.setItem('token', data.token);
      localStorage.setItem('username', data.username);
      msg.textContent = '✓ Account created!';
      setTimeout(() => location.href = '/', 600);
    } catch (err) { msg.textContent = '✗ ' + err.message; }
  });
}
