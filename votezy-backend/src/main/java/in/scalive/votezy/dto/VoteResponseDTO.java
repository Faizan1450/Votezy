package in.scalive.votezy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class VoteResponseDTO {
	private String message;
	private Integer VoterId;
	private Integer CandidateId;
}
