package in.scalive.votezy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.votezy.dto.VoteRequestDTO;
import in.scalive.votezy.dto.VoteResponseDTO;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.service.VoteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vote")
@CrossOrigin
public class VoteController {
	private VoteService voteService;
	
	public VoteController(VoteService voteService) {
		this.voteService = voteService;
	}
	
	@PostMapping("/cast")
	public ResponseEntity<VoteResponseDTO> castVote(@RequestBody @Valid VoteRequestDTO voteRequest) {
		Vote vote = voteService.castVote(voteRequest.getVoterId(), voteRequest.getCandidateId());
		VoteResponseDTO voteResponseDTO = new VoteResponseDTO("Vote Casted Successfully", vote.getVoterId(), vote.getCandidateId());
		return ResponseEntity.ok(voteResponseDTO);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Vote>> getAllVotes(){
		List<Vote> voteList = voteService.getAllVotes();
		return ResponseEntity.ok(voteList);
	}
}
