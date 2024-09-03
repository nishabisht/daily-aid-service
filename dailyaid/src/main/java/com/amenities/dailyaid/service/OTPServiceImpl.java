package com.amenities.dailyaid.service;

import com.amenities.dailyaid.model.OTP;
import com.amenities.dailyaid.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
public class OTPServiceImpl implements OTPService{
    @Autowired
    private OTPRepository otpRepository;

    @Override
    public void generateAndSentOTP(String mobileNumber) {
        //generate OTP
        String otp= String.format("%04d",new Random().nextInt(10000));

        //set expiry time
        LocalDateTime expiryDate= LocalDateTime.now().plusMinutes(10);

        //save OTP to database
        OTP otpEntity= new OTP();
        otpEntity.setMobileNumber(mobileNumber);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryDate(expiryDate);

        otpRepository.save(otpEntity);

        //sent SMS from third party vendor
    }

    @Override
    public boolean validate(String mobileNumber, String otp) {
        OTP savedOTP= otpRepository.findByMobileNumber(mobileNumber);

        if (savedOTP!=null && Objects.equals(savedOTP.getOtp(), otp) && savedOTP.getExpiryDate().isAfter(LocalDateTime.now())){
            return true;
        }
        return false;
    }
}
