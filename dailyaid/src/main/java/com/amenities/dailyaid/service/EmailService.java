package com.amenities.dailyaid.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendResetPasswordEmail(String email, String token){

        //Use JavaMailSender or another email library
        String resetUrl= "https://your-app.com/reset-password?token="+token;
        String message= "Click on the following link to reset your password:"+resetUrl;
    }
}
