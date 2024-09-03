package com.amenities.dailyaid.controller;

import com.amenities.dailyaid.exception.ApplicationException;
import com.amenities.dailyaid.model.UserData;
import com.amenities.dailyaid.service.OTPService;
import com.amenities.dailyaid.service.UserService;
import com.amenities.dailyaid.service.ResetPassword;
import com.amenities.dailyaid.service.ResetPasswordRequest;
import com.amenities.dailyaid.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;


@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private OTPService otpService;

    // Endpoint for user authentication through mobile number & OTP
    @PostMapping("/request-otp/{mobileNumber}")
    public ResponseEntity<String> requestOtp(@RequestParam String mobileNumber){
        otpService.generateAndSentOTP(mobileNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body("OTP generated");
    }

    @PostMapping("/validate-otp/{mobileNumber}/{otp}")
    public ResponseEntity<String> validateOtp(@RequestParam String mobileNumber,@RequestParam String otp){
        boolean isValid= otpService.validate(mobileNumber,otp);
        if (isValid){
            //Implement JWT token generation here
            return ResponseEntity.ok("OTP validated successfully");
        }else {
            return ResponseEntity.status(400).body("Invalid or expired OTP");
        }
    }


    // Endpoint for user authentication through username & password
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            // Authenticate the user using their username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // Load the user details
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        // Generate a JWT token for the user
        final String jwtToken = jwtUtil.generateToken(userDetails);

        // Return the JWT token in the response
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }

    //Endpoint to request a password reset
    @PostMapping("/request-reset-password")
    public ResponseEntity<String> requestPasswordReset(@RequestBody ResetPasswordRequest request){
        try{
            userService.createPasswordResetToken(request.getEmail());
            return ResponseEntity.ok("Password reset link has been sent to your email.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send reset link.");
        }
    }

    //Endpoint to handle the password reset
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassword resetPassword){
        try{
            userService.resetPassword(resetPassword.getToken(), resetPassword.getNewPassword());
            return ResponseEntity.ok("Password has been reset successfully.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reset password.");
        }
    }

   //Endpoint to save user details
    @PostMapping("/saveUser")
    public ResponseEntity<UserData> saveUserDetail(@RequestBody UserData userData){
        UserData savedUser=userService.saveUser(userData);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    //Endpoint to get user details
    @GetMapping("/getUser/{username}")
    public ResponseEntity<UserData> getUserDetail(@PathVariable String username) throws ApplicationException {
        UserData user=userService.getUser(username);
        return ResponseEntity.status(HttpStatus.FOUND).body(user);
    }
}
