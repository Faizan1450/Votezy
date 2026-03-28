package in.scalive.votezy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequestDTO {
	@NotNull(message="Voter ID is required")
	private Integer VoterId;
	@NotNull(message="Candidate ID is required")
	private Integer CandidateId;
}
