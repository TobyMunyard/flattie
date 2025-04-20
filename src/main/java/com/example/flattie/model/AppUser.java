package com.example.flattie.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entity that represents a user account within the system. Implements
 * UserDetails in order to use spring security and be passed to the
 * front-end/model easily.
 */
@Entity
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name cannot be null")
    private String firstName;

    @NotBlank(message = "Last name cannot be null")
    private String lastName;

    @Column(unique = true)
    @NotBlank(message = "Username cannot be null")
    @Size(min = 5, message = "Username must be at least 5 characters long")
    private String username;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 7, message = "Password must be at least 7 characters long")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY) // Many users can belong to one flat
    @JoinColumn(name = "flat_id", referencedColumnName = "id", nullable = true)
    @JsonIgnore // Prevents circular reference when serializing to JSON
    private Flat flat;
    
    private String profileImage; // URL to the user's profile image

    public AppUser() {
    }

    public AppUser(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    @Override
    public String toString() {
        return "AppUser [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
                + ", password=" + password + "]";
    }

    // WILL UPDATE ALL OF THESE LATER, JUST TEMP LIKE THIS

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
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

    public void setProfileImage(String path) {
        this.profileImage = path;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getRole() {
        return "ROLE_USER"; // Placeholder for role, can be updated later
    }
}
