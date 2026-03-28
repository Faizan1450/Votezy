package in.scalive.votezy.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class Candidate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message="Name is required")
	private String name;
	@NotBlank(message="Party is required")
	private String party;
	
	private Integer voteCount = 0;
	
	@OneToMany(mappedBy="candidate", cascade=CascadeType.ALL)
	@JsonIgnore
	private List<Vote> vote;
}
