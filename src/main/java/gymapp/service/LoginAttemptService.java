package gymapp.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPT = 3;
    private static final long BLOCK_DURATION_MINUTES = 5;

    private final LoadingCache<String, Integer> attemptsCache;
    private final LoadingCache<String, Long> blockCache;

    public LoginAttemptService() {
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });

        blockCache = CacheBuilder.newBuilder()
                .expireAfterWrite(BLOCK_DURATION_MINUTES, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public Long load(String key) {
                        return 0L;
                    }
                });
    }

    public void loginFailed(String clientIP) {
        try {
            int attempts = attemptsCache.get(clientIP) + 1;
            attemptsCache.put(clientIP, attempts);

            if (attempts >= MAX_ATTEMPT) {
                blockCache.put(clientIP, System.currentTimeMillis());
            }
        } catch (ExecutionException e) {
            attemptsCache.put(clientIP, 1);
        }
    }

    public boolean isBlocked(String clientIP) {
        try {
            return attemptsCache.get(clientIP) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }

    public void loginSucceeded(String clientIP) {
        attemptsCache.invalidate(clientIP);
        blockCache.invalidate(clientIP);
    }

    public String getClientIP(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty()) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}