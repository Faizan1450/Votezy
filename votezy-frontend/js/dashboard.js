import { Api } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
  const voterId = localStorage.getItem('voterId');
  const voterName = localStorage.getItem('voterName');
  
  if (!voterId) {
    window.location.href = 'index.html';
    return;
  }

  if (voterName) {
    document.getElementById('voterNameDisplay').textContent = `Welcome, ${voterName}`;
  }

  document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('voterId');
    localStorage.removeItem('voterName');
    window.location.href = 'index.html';
  });

  const grid = document.getElementById('candidatesGrid');
  const badge = document.getElementById('voteStatusBadge');
  const statusBanner = document.getElementById('statusBanner');
  let hasVoted = false;
  let candidates = [];

  try {
    // 1. Fetch voter status
    const voter = await Api.getVoter(voterId);
    hasVoted = voter.hasVoted;
    
    if (hasVoted) {
      badge.textContent = 'Status: Voted ✓';
      badge.className = 'inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-green-100 text-green-800';
      statusBanner.classList.remove('hidden');
    } else {
      badge.textContent = 'Status: Not Voted';
      badge.className = 'inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-yellow-100 text-yellow-800';
    }

    // 2. Fetch candidates
    candidates = await Api.getCandidates();
    renderCandidates(candidates, hasVoted);

  } catch (err) {
    console.error(err);
    grid.innerHTML = '<div class="col-span-full text-center text-red-500 py-10 text-lg">Failed to load data. Ensure the backend is running on port 8080.</div>';
  }

  // --- Event Delegation: attach one listener to the grid container ---
  grid.addEventListener('click', async (e) => {
    const btn = e.target.closest('.vote-btn');
    if (!btn || hasVoted) return;

    const candidateId = btn.getAttribute('data-id');
    const candidateName = btn.getAttribute('data-name');

    // Show a custom inline confirmation instead of native confirm()
    showConfirmModal(candidateName, async () => {
      btn.textContent = 'Casting vote...';
      btn.disabled = true;
      btn.classList.add('opacity-75', 'cursor-not-allowed');

      try {
        const result = await Api.castVote({ VoterId: parseInt(voterId), CandidateId: parseInt(candidateId) });
        if (result && result.message) {
          hasVoted = true;
          badge.textContent = 'Status: Voted ✓';
          badge.className = 'inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-green-100 text-green-800';
          statusBanner.classList.remove('hidden');
          renderCandidates(candidates, true); // Re-render all buttons as disabled
        } else {
          throw new Error(result.message || 'Unknown error');
        }
      } catch (err) {
        console.error('Vote failed:', err);
        btn.textContent = `Vote for ${candidateName}`;
        btn.disabled = false;
        btn.classList.remove('opacity-75', 'cursor-not-allowed');
        showToast('Failed to cast vote. You may have already voted or the server is unavailable.', 'error');
      }
    });
  });
});

function renderCandidates(candidates, hasVoted) {
  const grid = document.getElementById('candidatesGrid');
  grid.innerHTML = '';

  if (candidates.length === 0) {
    grid.innerHTML = '<div class="col-span-full text-center text-gray-500 py-10 text-lg">No candidates registered yet.</div>';
    return;
  }

  candidates.forEach(candidate => {
    const card = document.createElement('div');
    card.className = 'bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden hover:shadow-md transition-shadow duration-200';

    const btnHTML = hasVoted
      ? `<button disabled class="w-full mt-6 py-2.5 px-4 rounded-lg text-sm font-semibold text-white bg-gray-400 cursor-not-allowed">Voting Closed</button>`
      : `<button data-id="${candidate.id}" data-name="${candidate.name}" class="vote-btn w-full mt-6 py-2.5 px-4 rounded-lg text-sm font-semibold text-white bg-primary hover:bg-indigo-700 active:scale-95 transition-all cursor-pointer">Vote for ${candidate.name}</button>`;

    card.innerHTML = `
      <div class="px-6 py-8 h-full flex flex-col justify-between">
        <div>
          <div class="inline-flex items-center justify-center p-3 bg-indigo-50 rounded-lg mb-4 text-primary">
            <svg class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>
          </div>
          <h3 class="text-xl font-bold text-gray-900">${candidate.name}</h3>
          <p class="text-sm font-medium text-gray-500 mt-1 uppercase tracking-wide">${candidate.party}</p>
        </div>
        ${btnHTML}
      </div>
    `;
    grid.appendChild(card);
  });
}

function showConfirmModal(candidateName, onConfirm) {
  // Remove existing modal if any
  document.getElementById('confirmModal')?.remove();
  
  const modal = document.createElement('div');
  modal.id = 'confirmModal';
  modal.className = 'fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50';
  modal.innerHTML = `
    <div class="bg-white rounded-2xl shadow-2xl p-8 max-w-sm w-full mx-4 transform transition-all">
      <div class="text-center mb-6">
        <div class="inline-flex items-center justify-center p-3 bg-indigo-100 rounded-full mb-4">
          <svg class="h-8 w-8 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
        </div>
        <h3 class="text-xl font-bold text-gray-900">Confirm Your Vote</h3>
        <p class="mt-2 text-gray-600">You are voting for <strong class="text-primary">${candidateName}</strong>. This action cannot be undone.</p>
      </div>
      <div class="flex gap-3">
        <button id="cancelVote" class="flex-1 py-2.5 px-4 border border-gray-300 rounded-lg text-gray-700 font-semibold hover:bg-gray-50 transition-colors">Cancel</button>
        <button id="confirmVote" class="flex-1 py-2.5 px-4 bg-primary rounded-lg text-white font-semibold hover:bg-indigo-700 transition-colors">Confirm Vote</button>
      </div>
    </div>
  `;
  document.body.appendChild(modal);
  
  document.getElementById('cancelVote').addEventListener('click', () => modal.remove());
  document.getElementById('confirmVote').addEventListener('click', () => {
    modal.remove();
    onConfirm();
  });
  // Close on backdrop click
  modal.addEventListener('click', (e) => { if (e.target === modal) modal.remove(); });
}

function showToast(message, type = 'info') {
  const toast = document.createElement('div');
  const bg = type === 'error' ? 'bg-red-600' : 'bg-green-600';
  toast.className = `fixed bottom-6 right-6 z-50 ${bg} text-white px-6 py-3 rounded-lg shadow-lg text-sm font-medium transition-all`;
  toast.textContent = message;
  document.body.appendChild(toast);
  setTimeout(() => toast.remove(), 4000);
}
