package gymapp.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RestLoggingAspect {

    private final ObjectMapper mapper = new ObjectMapper();

    @Around("execution(* controller.v1..*(..))")
    public Object logRestCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("REST call started: {}", methodName);
        try {

            Object result = joinPoint.proceed();

            if (result instanceof ResponseEntity<?> response) {
                log.info("REST call success: {} with status {}", methodName, response.getStatusCode());
            } else {
                log.info("REST call success: {}", methodName);
            }

            return result;
        } catch (Exception e) {
            log.error("REST call failed: {} - {}", methodName, e.getMessage());
            throw e;
        }
    }
}