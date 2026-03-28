package in.scalive.votezy.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.exception.DuplicateResourceException;
import in.scalive.votezy.exception.ResourceNotFoundException;
import in.scalive.votezy.repository.CandidateRepository;
import in.scalive.votezy.repository.VoterRepository;

@Service
public class VoterService {
	private VoterRepository voterRepository;
	private CandidateRepository candidateRepository;
	
	public VoterService(VoterRepository voterRepository, CandidateRepository candidateRepository) {
		this.voterRepository = voterRepository;
		this.candidateRepository = candidateRepository;
	}
	
	public Voter registerVoter(Voter voter) {
		if(voterRepository.existsByEmail(voter.getEmail())) {
			throw new DuplicateResourceException("Voter with email id: " + voter.getEmail() + " already Exists");
		}
		return voterRepository.save(voter);
	}
	
	public List<Voter> getAllVoters() {
		return voterRepository.findAll();
	}
	
	public Voter getVoterById(Integer voterId) {
		return voterRepository.findById(voterId).orElseThrow(() -> new ResourceNotFoundException("Voter with id: " + voterId + " not found"));
	}
	
	public Voter updateVoter(Integer voterId, Voter v) {
		Voter voter = voterRepository.findById(voterId).orElseThrow(()-> new ResourceNotFoundException("Voter with id: " + voterId + " not found"));
		if(v.getName() != null) {
			voter.setName(v.getName());
		}
		if(v.getEmail() != null) {
			voter.setEmail(v.getEmail());
		}
		return voterRepository.save(voter);
	}
	
	@Transactional
	public Voter deleteVoter(Integer voterId) {
		Voter voter = voterRepository.findById(voterId).orElseThrow(()-> new ResourceNotFoundException("Cannot Delete Voter with id: " + voterId));
		Vote vote = voter.getVote();
		if(vote != null) {
			Candidate candidate = vote.getCandidate();
			candidate.setVoteCount(candidate.getVoteCount() - 1);
		}
		
		voterRepository.delete(voter);
		return voter;
	}
	
	
}
