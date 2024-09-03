package com.amenities.dailyaid.service;

import com.amenities.dailyaid.exception.ApplicationException;
import com.amenities.dailyaid.model.UserData;

public interface UserService {

    UserData saveUser(UserData userData);

    UserData getUser(String username) throws ApplicationException;

    public void resetPassword(String token, String newPassword) throws ApplicationException;

    public void createPasswordResetToken(String email) throws ApplicationException;
}
