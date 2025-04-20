package pl.cleankod.util.logging;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

public class TraceIdFilter implements Filter {
    private static final String TRACE_HEADER = "X-Request-ID";
    private static final String TRACE_KEY = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String traceId = httpRequest.getHeader(TRACE_HEADER);

            if (traceId == null || traceId.isBlank()) {
                traceId = UUID.randomUUID().toString();
            }

            MDC.put(TRACE_KEY, traceId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_KEY);
        }
    }
}
