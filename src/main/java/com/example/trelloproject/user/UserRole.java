package com.example.trelloproject.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
	ADMIN(0,"관리자"), USER(1, "사용자");

	private final Integer statusNumber;
	private final String statusName;
}
