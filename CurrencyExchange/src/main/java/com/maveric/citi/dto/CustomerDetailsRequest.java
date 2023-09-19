package com.maveric.citi.dto;

import com.maveric.citi.enums.GenderEnum;
import com.maveric.citi.enums.RoleEnum;
import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern.Flag;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailsRequest 
{
	
	private String customerName;
	private String password;
	private GenderEnum gender;
	private RoleEnum role;


	
	@Email(message = "Email is not valid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",flags = Flag.CASE_INSENSITIVE)
	@NotEmpty(message = "Email cannot be empty")
	private String email;
	private Long contactNumber;
	
	
	
	
	@Override
	public String toString() {
		return "CustomerDetails [ customerName=" + customerName + ", gender=" + gender
				+ ", role=" + role + ", email=" + email + ", contactNumber=" + contactNumber + ", OnboardDate="
				+  "]";
	}
}
