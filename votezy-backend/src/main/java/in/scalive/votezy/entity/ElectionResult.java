package in.scalive.votezy.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class ElectionResult {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message="Election Name is required")
	private String electionName;
	
	@OneToOne
	@JoinColumn(name="winner_id")
	private Candidate winnerCandidate;
	
	@JsonProperty("WinnerId")
	public Integer getWinnerId() {
		return winnerCandidate.getId();
	}
	
	private Integer totalVotes = 0;
}
