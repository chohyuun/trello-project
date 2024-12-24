package com.example.trelloproject.user;

import com.example.trelloproject.global.entity.BaseEntity;
import com.example.trelloproject.user.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user")
@Getter
@Entity
@NoArgsConstructor
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", unique = true)
	private String email;

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	@Column(nullable = false)
	private boolean isDelete = false;

	public User(String email, String password, String userName, UserRole role) {
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.role = role;
	}

	public boolean getIsDelete(){
		return isDelete;
	}
}
