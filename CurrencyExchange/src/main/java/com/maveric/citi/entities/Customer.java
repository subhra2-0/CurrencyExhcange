package com.maveric.citi.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;



import com.maveric.citi.enums.GenderEnum;
import com.maveric.citi.enums.RoleEnum;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements UserDetails
{
	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", account=" + account + ", customerName=" + customerName
				+ ", gender=" + gender + ", email=" + email + ", contactNumber=" + contactNumber + ", OnboardDate="
				+ onboardDate + ", role=" + role + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;
	
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Account> account;

	@OneToMany(cascade = CascadeType.ALL)
	private List<CurrencyConversion> currencyConversions;
	
	@Column(nullable = false)
	private String customerName;

	@Column(nullable = false)
	private String password;
	
	@Column
	@Enumerated(EnumType.STRING)
	private GenderEnum gender;
	
	@Column(unique = true,nullable = false)
	private String email;
	
	@Column(nullable = false)
	private Long contactNumber;
	
	@Column(updatable = false)
	private LocalDateTime onboardDate;
	
	@Column(nullable= false)
	@Enumerated(EnumType.STRING)
	private RoleEnum role;


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
