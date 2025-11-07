package gymapp.configuration;

import gymapp.service.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements
        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final HttpServletRequest request;
    private final LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        String clientIP = (xfHeader != null && !xfHeader.isEmpty()) ? xfHeader.split(",")[0] : request.getRemoteAddr();
        loginAttemptService.loginFailed(clientIP);
    }
}