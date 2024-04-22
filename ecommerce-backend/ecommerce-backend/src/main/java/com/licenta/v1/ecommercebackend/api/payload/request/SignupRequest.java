package com.licenta.v1.ecommercebackend.api.payload.request;

import jakarta.validation.constraints.*;

import java.util.Set;

public class SignupRequest {

    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    @Size(min = 3)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")
    @Size(max = 32)
    private String password;
    @NotNull
    @NotBlank
    @Email
    private String email;

    private Set<String> role;

    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    @NotNull
    @NotBlank
    private String city;
    @NotNull
    @NotBlank
    private String country;
    @NotNull
    @NotBlank
    private String addressLine1;

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
