package com.example.trelloproject.user;

import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);


	Optional<User> findByEmail(String email);
	Optional<User> findById(Long id);
	Optional<User> findByUserName(String username);

	default User findByEmailOrElseThrow(String email){
		User user =  findByEmail(email).orElseThrow(
			() -> new BusinessException(ExceptionType.USER_NOT_FOUNT)
		);
		if (user.getIsDelete()){
			throw new BusinessException(ExceptionType.USER_DELETED);
		}
		return user;
	}

	default User findByIdOrElseThrow(Long id){
		User user = findById(id).orElseThrow(
			() -> new BusinessException(ExceptionType.USER_NOT_FOUNT)
		);
		if (user.getIsDelete()){
			throw new BusinessException(ExceptionType.USER_DELETED);
		}
		return user;
	}

	default User findByUsernameOrElseThrow(String username){
		User user = findByUserName(username).orElseThrow(
			() -> new BusinessException(ExceptionType.USER_NOT_FOUNT)
		);
		if (user.getIsDelete()){
			throw new BusinessException(ExceptionType.USER_DELETED);
		}
		return user;
	}

}
