package in.scalive.votezy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ElectionRequestDTO {
	@NotBlank(message="Election name is required")
	private String electionName;
}
