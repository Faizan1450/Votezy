const BASE_URL = 'http://localhost:8080/api';

export const Api = {
  // --- Voters ---
  async registerVoter(voterData) {
    const res = await fetch(`${BASE_URL}/voters`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(voterData)
    });
    return res.json();
  },
  async getVoter(voterId) {
    const res = await fetch(`${BASE_URL}/voters/${voterId}`);
    return res.json();
  },

  // --- Candidates ---
  async getCandidates() {
    const res = await fetch(`${BASE_URL}/candidates`);
    return res.json();
  },
  async registerCandidate(candidateData) {
    const res = await fetch(`${BASE_URL}/candidates`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(candidateData)
    });
    return res.json();
  },

  // --- Voting ---
  async castVote(voteRequest) {
    const res = await fetch(`${BASE_URL}/vote/cast`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(voteRequest)
    });
    return res.json();
  },

  // --- Election Results ---
  async getElectionResults() {
    const res = await fetch(`${BASE_URL}/election-result/`);
    return res.json();
  },
  async declareElectionResult(electionName) {
    const res = await fetch(`${BASE_URL}/election-result/declare`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ electionName })
    });
    return res.json();
  }
};
