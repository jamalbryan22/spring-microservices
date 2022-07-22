package com.bryanscode.customer;

public record CustomerRegistrationRequest(
    String firstName,
    String lastName,
    String email) {
}
