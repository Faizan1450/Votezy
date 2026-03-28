package in.scalive.votezy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ElectionResponseDTO {
	private String electionName;
	private Integer totalVotes;
	private Integer winnerId;
	private Integer winnerVoteCount;
}
