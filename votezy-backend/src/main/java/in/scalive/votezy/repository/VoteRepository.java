package in.scalive.votezy.repository;

import in.scalive.votezy.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Integer>{
	
}
