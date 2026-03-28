import { Api } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
  
  const addCandidateForm = document.getElementById('addCandidateForm');
  const declareResultForm = document.getElementById('declareResultForm');
  const candidatesTableBody = document.getElementById('candidatesTableBody');
  const resultsGrid = document.getElementById('resultsGrid');

  async function loadData() {
    try {
      // Load Candidates
      const candidates = await Api.getCandidates();
      candidatesTableBody.innerHTML = '';
      if (candidates.length === 0) {
        candidatesTableBody.innerHTML = '<tr><td colspan="3" class="px-6 py-4 text-center text-sm text-gray-500">No candidates found</td></tr>';
      } else {
        candidates.forEach(c => {
          candidatesTableBody.innerHTML += `
            <tr>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${c.name}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${c.party}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-bold text-primary">${c.voteCount}</td>
            </tr>
          `;
        });
      }

      // Load Results
      const results = await Api.getElectionResults();
      resultsGrid.innerHTML = '';
      if (results.length === 0) {
        resultsGrid.innerHTML = '<div class="col-span-full text-sm text-gray-500 italic">No past results declared.</div>';
      } else {
        results.forEach(r => {
          resultsGrid.innerHTML += `
            <div class="border border-green-200 bg-green-50 rounded-lg p-4 transition hover:shadow-md">
              <h3 class="text-lg font-bold border-b border-green-200 pb-2 mb-2 text-green-900">${r.electionName}</h3>
              <p class="text-sm text-green-800">Winner ID: <b>${r.WinnerId}</b></p>
              <p class="text-sm text-green-800 mt-1">Total Votes Cast: <b>${r.totalVotes}</b></p>
            </div>
          `;
        });
      }
    } catch (err) {
      console.error(err);
    }
  }

  // Add Candidate
  addCandidateForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('candidateName').value;
    const party = document.getElementById('candidateParty').value;
    
    try {
      await Api.registerCandidate({ name, party });
      addCandidateForm.reset();
      showToast(`Candidate "${name}" registered!`, 'success');
      loadData();
    } catch (err) {
      console.error(err);
      showToast('Failed to register candidate.', 'error');
    }
  });

  // Declare Election
  declareResultForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const electionName = document.getElementById('electionName').value;

    showConfirmModal(`Declare results for "${electionName}"?`, async () => {
      try {
        await Api.declareElectionResult(electionName);
        declareResultForm.reset();
        showToast('Election results declared!', 'success');
        loadData();
      } catch (err) {
        console.error(err);
        showToast('Failed to declare results. Ensure there are votes cast.', 'error');
      }
    });
  });

  // Initial Load
  loadData();
});

function showConfirmModal(message, onConfirm) {
  document.getElementById('confirmModal')?.remove();
  const modal = document.createElement('div');
  modal.id = 'confirmModal';
  modal.className = 'fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50';
  modal.innerHTML = `
    <div class="bg-white rounded-2xl shadow-2xl p-8 max-w-sm w-full mx-4">
      <h3 class="text-xl font-bold text-gray-900 mb-3">Confirm Action</h3>
      <p class="text-gray-600 mb-6">${message}</p>
      <div class="flex gap-3">
        <button id="cancelAction" class="flex-1 py-2.5 px-4 border border-gray-300 rounded-lg text-gray-700 font-semibold hover:bg-gray-50 transition-colors">Cancel</button>
        <button id="confirmAction" class="flex-1 py-2.5 px-4 bg-red-600 rounded-lg text-white font-semibold hover:bg-red-700 transition-colors">Confirm</button>
      </div>
    </div>
  `;
  document.body.appendChild(modal);
  document.getElementById('cancelAction').addEventListener('click', () => modal.remove());
  document.getElementById('confirmAction').addEventListener('click', () => { modal.remove(); onConfirm(); });
  modal.addEventListener('click', (e) => { if (e.target === modal) modal.remove(); });
}

function showToast(message, type = 'info') {
  const toast = document.createElement('div');
  const bg = type === 'error' ? 'bg-red-600' : 'bg-green-600';
  toast.className = `fixed bottom-6 right-6 z-50 ${bg} text-white px-6 py-3 rounded-lg shadow-lg text-sm font-medium`;
  toast.textContent = message;
  document.body.appendChild(toast);
  setTimeout(() => toast.remove(), 4000);
}
