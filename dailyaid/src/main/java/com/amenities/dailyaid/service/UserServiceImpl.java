package com.amenities.dailyaid.service;

import com.amenities.dailyaid.exception.ApplicationException;
import com.amenities.dailyaid.model.PasswordResetToken;
import com.amenities.dailyaid.model.UserData;
import com.amenities.dailyaid.repository.PasswordResetTokenRepository;
import com.amenities.dailyaid.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    public static final Logger log= LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService; //A service to sent emails

    //reset password service
    @Override
    public void resetPassword(String token, String newPassword) throws ApplicationException {
        PasswordResetToken resetToken= tokenRepository.findByToken(token).orElseThrow(()->new ApplicationException("Invalid or expired token"));
        log.info("token id : {}",resetToken);
        if(resetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new ApplicationException("Token has expired");
        }

        UserData user= resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken);//Clean up the token
    }

    @Override
    public void createPasswordResetToken(String email) throws ApplicationException {
        UserData user= userRepository.findByEmail(email).orElseThrow(()->new ApplicationException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token,user);

        tokenRepository.save(resetToken);
        emailService.sendResetPasswordEmail(user.getEmail(),token);
    }

    @Override
    public UserData saveUser(UserData userData) {
        //encrypt the password before saving
        String encryptedPassword= passwordEncoder.encode(userData.getPassword());
        userData.setPassword(encryptedPassword);

        UserData savedUser=userRepository.save(userData);
        if (savedUser.getUsername() == null || savedUser.getUsername().isEmpty()){
            log.error("Failed to save user: saved user is null or username is empty");
        }else {
            log.info("User saved successfully : "+ savedUser.toString());
        }
        return savedUser;
    }

    @Override
    public UserData getUser(String username) throws ApplicationException {
        UserData user= userRepository.findByUsername(username).orElseThrow(()-> new ApplicationException("user not found"));
        log.info("User found : {}", user.toString());
        return user;
    }
}
