package com.amenities.dailyaid.service;

import com.amenities.dailyaid.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public interface OTPService {

    public void generateAndSentOTP(String mobileNumber);

    public boolean validate(String mobileNumber,String otp);
}
