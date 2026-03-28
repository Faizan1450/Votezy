package in.scalive.votezy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.ElectionResult;
import in.scalive.votezy.repository.CandidateRepository;
import in.scalive.votezy.repository.ElectionResultRepository;
import in.scalive.votezy.repository.VoteRepository;

@Service
public class ElectionResultService {
	private ElectionResultRepository electionResultRepository;
	private CandidateRepository candidateRepository;
	private VoteRepository voteRepository;
	
	public ElectionResultService(ElectionResultRepository electionResultRespository, CandidateRepository candidateRepository, VoteRepository voteRepository) {
		this.electionResultRepository = electionResultRespository;
		this.candidateRepository = candidateRepository;
		this.voteRepository = voteRepository;
	}
	
	public ElectionResult declareElectionResult(String electionName) {
		Optional<ElectionResult> existingElectionResult = electionResultRepository.findByElectionName(electionName);
		if(existingElectionResult.isPresent()) {
			return existingElectionResult.get();
		}
		
		Long voteCount = voteRepository.count();
		if(voteCount == 0) {
			throw new IllegalStateException("Cannot declare the result as no one voted yet");
		}
		List<Candidate> candidateList = candidateRepository.findAllByOrderByVoteCountDesc();
		Candidate winnerCandidate = candidateList.get(0);
		
		Integer totalVotes = 0;
		for(Candidate candidate: candidateList) {
			totalVotes += candidate.getVoteCount();
		}
		
		ElectionResult electionResult = new ElectionResult();
		electionResult.setElectionName(electionName);
		electionResult.setTotalVotes(totalVotes);
		electionResult.setWinnerCandidate(winnerCandidate);
		electionResultRepository.save(electionResult);
		return electionResult;
	}
	
	public List<ElectionResult> getAllResults(){
		return electionResultRepository.findAll();
	}
}
