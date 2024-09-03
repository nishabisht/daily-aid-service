package com.amenities.dailyaid.controller;

public class AuthenticationResponse {

    private String jwt = null;
    public AuthenticationResponse(String jwtToken) {
        this.jwt = jwtToken;
    }

    public String getJwt(){
        return jwt;
    }
}
