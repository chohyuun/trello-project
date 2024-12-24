package com.example.trelloproject.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);


	Optional<User> findByEmail(String email);
	Optional<User> findById(Long id);

	default User findByEmailOrElseThrow(String email){
		return findByEmail(email).orElseThrow(
			() -> new RuntimeException("User not found")
		);
	}

	default User findByIdOrElseThrow(Long id){
		return findById(id).orElseThrow(
			() -> new RuntimeException("User not found")
		);
	}
}
