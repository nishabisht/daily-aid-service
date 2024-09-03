package com.amenities.dailyaid.service;

import com.amenities.dailyaid.model.UserData;
import com.amenities.dailyaid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Fetch user from database

        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Convert the User object to a UserDetails object
        return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))// Assuming roles are stored as a list of strings
                .build();
    }


}
