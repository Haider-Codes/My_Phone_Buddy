package com.myphonebuddy.utility.utils;

import com.myphonebuddy.constants.AppConstants;
import org.springframework.stereotype.Component;

@Component
public class EmailVerificationUtil {

    public static String generateEmailVerificationLink(String verifyToken) {
        return AppConstants.APP_DOMAIN+"/auth/verify-email?token="+verifyToken;
    }

}
