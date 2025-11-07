package gymapp.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class TransactionLoggingFilter implements Filter {

    private static final String TX_ID_KEY = "transactionId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            String transactionId = UUID.randomUUID().toString();
            MDC.put(TX_ID_KEY, transactionId);

            if (request instanceof HttpServletRequest httpReq) {
                log.debug("Starting request: {} {} [tx:{}]",
                        httpReq.getMethod(), httpReq.getRequestURI(), transactionId);
            }

            chain.doFilter(request, response);

        } finally {
            MDC.remove(TX_ID_KEY);
        }
    }
}