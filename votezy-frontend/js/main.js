import { Api } from './api.js';

document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('registerForm');
  const errorMsg = document.getElementById('errorMsg');
  const btnText = document.getElementById('btnText');
  const btnLoader = document.getElementById('btnLoader');

  // If already registered in this session, auto redirect
  if (localStorage.getItem('voterId')) {
    window.location.href = 'dashboard.html';
  }

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    errorMsg.classList.add('hidden');
    
    const name = document.getElementById('voterName').value;
    const email = document.getElementById('voterEmail').value;

    try {
      btnText.innerText = 'Registering...';
      btnLoader.classList.remove('hidden');

      const data = await Api.registerVoter({ name, email });
      if (data && data.id) {
        localStorage.setItem('voterId', data.id);
        localStorage.setItem('voterName', data.name);
        window.location.href = 'dashboard.html';
      } else {
        throw new Error('Invalid response');
      }
    } catch (err) {
      console.error(err);
      errorMsg.classList.remove('hidden');
    } finally {
      btnText.innerText = 'Register & Enter';
      btnLoader.classList.add('hidden');
    }
  });
});
