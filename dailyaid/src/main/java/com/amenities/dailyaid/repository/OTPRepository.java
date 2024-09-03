package com.amenities.dailyaid.repository;

import com.amenities.dailyaid.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<OTP,Long> {

    OTP findByMobileNumber(String mobileNumber);

}
