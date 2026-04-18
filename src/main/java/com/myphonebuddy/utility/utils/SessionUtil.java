package com.myphonebuddy.utility.utils;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionUtil {

    // Use to remove the session attribute
    public static void removeAcknowledgement() {
        /* We're using RequestContextHolder static class instead of HttpServletRequest because we want to get
           session, without having 'request' object explicitly because our view won't have any request object
           by default, which is necessary to use for HttpServletRequest.
         */
        try {
            HttpSession httpSession = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession(true);
            httpSession.removeAttribute("acknowledgement");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
