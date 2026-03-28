package in.scalive.votezy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.service.VoterService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/voters")
@CrossOrigin
public class VoterController {
	private VoterService voterService;
	public VoterController(VoterService voterService) {
		this.voterService = voterService;
	}
	
	@PostMapping
	public ResponseEntity<Voter> registerVoter(@RequestBody @Valid Voter v) {
		Voter voter = voterService.registerVoter(v);
		return new ResponseEntity<>(voter, HttpStatus.CREATED);
	}
	
	@GetMapping("/{voterId}")
	public ResponseEntity<Voter> getVoter(@PathVariable Integer voterId) {
		Voter voter = voterService.getVoterById(voterId);
		return new ResponseEntity<>(voter, HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Voter>> getVoter() {
		List<Voter> voterList = voterService.getAllVoters();
		return ResponseEntity.ok(voterList);
	}
	
	@PutMapping("/{voterId}")
	public ResponseEntity<Voter> updateVoter(@PathVariable Integer voterId, @RequestBody Voter v){
		Voter voter = voterService.updateVoter(voterId, v);
		return new ResponseEntity<>(voter, HttpStatus.OK);
	}
	
	@DeleteMapping("/{voterId}")
	public ResponseEntity<Voter> deleteVoter(@PathVariable Integer voterId){
		Voter voter = voterService.deleteVoter(voterId);
		return new ResponseEntity<>(voter, HttpStatus.OK);
	}
}
