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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.service.CandidateService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin
public class CandidateController {
	private CandidateService candidateService;
	public CandidateController(CandidateService candidateService) {
		this.candidateService = candidateService;
	}
	
	@PostMapping
	public ResponseEntity<Candidate> registerCandidate(@RequestBody @Valid Candidate c){
		System.out.println(c);
		Candidate candidate =  candidateService.registerCandidate(c);
		return new ResponseEntity<>(candidate, HttpStatus.CREATED);
	}
	
	@GetMapping("/{candidateId}")
	public ResponseEntity<Candidate> getCandidate(@PathVariable Integer candidateId){
		Candidate candidate =  candidateService.getCandidateById(candidateId);
		return new ResponseEntity<>(candidate, HttpStatus.OK);
	} 
	
	@GetMapping
	public ResponseEntity<List<Candidate>> getAllCandidate(){
		List<Candidate> candidateList =  candidateService.getAllCandidates();
		return new ResponseEntity<>(candidateList, HttpStatus.OK);
	}
	
	@PutMapping("/{candidateId}")
	public ResponseEntity<Candidate> updateCandidate(@PathVariable Integer candidateId, @RequestBody Candidate c){
		Candidate candidate =  candidateService.updateCandidate(candidateId, c);
		return new ResponseEntity<>(candidate, HttpStatus.OK);
	}
	
	@DeleteMapping("/{candidateId}")
	public ResponseEntity<Candidate> deleteCandidate(@PathVariable Integer candidateId){
		Candidate candidate =  candidateService.deleteCandidate(candidateId);
		return new ResponseEntity<>(candidate, HttpStatus.OK);
	} 
}
