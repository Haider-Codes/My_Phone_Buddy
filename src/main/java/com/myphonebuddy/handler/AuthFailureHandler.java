package com.myphonebuddy.handler;

import com.myphonebuddy.enums.MessageType;
import com.myphonebuddy.modal.Acknowledgement;
import com.myphonebuddy.utility.utils.BuilderUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                        @NonNull AuthenticationException exception) throws IOException, ServletException {

        if(exception instanceof DisabledException) {

            HttpSession session = request.getSession(false);
            if(session != null) {
                var acknowledgement = BuilderUtil.buildAcknowledgementMsg(
                        MessageType.ERROR,
                        "Your account has been disabled"
                );
                session.setAttribute("acknowledgement", acknowledgement);
            }
            response.sendRedirect("/login");
        }
        else
            response.sendRedirect("/login?error=true");
    }
}
