package com.example.flattie.model;

import com.example.flattie.model.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import com.example.flattie.model.FlatMembershipStatus;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class FlatMembership {
	@Id
	private long id;

	@ManyToOne
	private AppUser user;

	@ManyToOne
	private Flat flat;

	public Flat getFlat() {
		return flat;
	}

	public void setFlat(Flat flat) {
		this.flat = flat;
	}

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private FlatMembershipStatus status;

	public FlatMembershipStatus getStatus() {
		return status;
	}

	public void setStatus(FlatMembershipStatus status) {
		this.status = status;
	}

	public Object getRole() {
		return this.role;
	}

	public void setRole(com.example.flattie.model.Role newRole) {
		this.role = newRole; // Set the role of the user in the flat
	}

	public AppUser getUser() {
		return user; // Return the associated AppUser
	}

	public void setUser(AppUser user) {
		this.user = user; // Set the associated AppUser
	}

}
