package com.maveric.citi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maveric.citi.enums.GenderEnum;
import com.maveric.citi.enums.RoleEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CustomerResponse {
	private String customerName;
	private GenderEnum gender;
	private RoleEnum role;
	private Long customerId;
	private String email;
	private Long contactNumber;
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	private LocalDateTime onboardDate;
}
