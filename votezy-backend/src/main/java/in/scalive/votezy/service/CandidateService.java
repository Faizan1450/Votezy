package in.scalive.votezy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.exception.ResourceNotFoundException;
import in.scalive.votezy.repository.CandidateRepository;

@Service
public class CandidateService {
	private CandidateRepository candidateRepository;
	
	public CandidateService(CandidateRepository candidateRepository) {
		this.candidateRepository = candidateRepository;
	}
	
	public Candidate registerCandidate(Candidate candidate) {
		return candidateRepository.save(candidate);
	}
	
	public Candidate getCandidateById(Integer candidateId) {
		return candidateRepository.findById(candidateId).orElseThrow(() -> new ResourceNotFoundException("Candidate with id: " + candidateId + " not found"));
	}
	
	public List<Candidate> getAllCandidates() {
		return candidateRepository.findAll();
	}
	
	public Candidate updateCandidate(Integer candidateId, Candidate c) {
		Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new ResourceNotFoundException("Candidate with id: " + candidateId + " not found"));
		if(c.getName() != null) {
			candidate.setName(c.getName());
		}
		if(c.getParty() != null) {
			candidate.setParty(c.getParty());
		}
		
		return candidate;
	}
	
	public Candidate deleteCandidate(Integer candidateId) {
		Candidate candidate =  candidateRepository.findById(candidateId).orElseThrow(() -> new ResourceNotFoundException("Candidate with id: " + candidateId + " not found"));
		List<Vote> voteList = candidate.getVote();
		for(Vote vote : voteList) {
			vote.setCandidate(null);
		}
		candidate.getVote().clear();
		candidateRepository.delete(candidate);
		return candidate;
	}
}
