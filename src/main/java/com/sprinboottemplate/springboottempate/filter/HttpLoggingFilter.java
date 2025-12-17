package com.sprinboottemplate.springboottempate.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class HttpLoggingFilter extends OncePerRequestFilter {
    private static final String MDC_KEY = "traceId";

    @Value("${app.logging.http.max-body-length:2000}")
    private int maxBodyLength;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long start = System.currentTimeMillis();

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String traceId = MDC.get(MDC_KEY);

        String method = requestWrapper.getMethod();
        String uri = requestWrapper.getRequestURI();
        String query = requestWrapper.getQueryString();

        String paramString = buildMaskedParamString(requestWrapper.getParameterMap());

        String requestBody = null;
        if (isBodyReadable(requestWrapper)) {
            requestBody = readRequestBody(requestWrapper);
            requestBody = truncate(requestBody, maxBodyLength);
            requestBody = maskBodyHeuristically(requestBody);
        }

        log.info(
                "[{}] {} {}{} params={} reqBody={}",
                traceId,
                method,
                uri,
                (query != null ? "?" + query : ""),
                emptyToDash(paramString),
                emptyToDash(requestBody)
        );

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long elapsed = System.currentTimeMillis() - start;

            String responseBody = null;
            if (isResponseBodyReadable(responseWrapper)) {
                responseBody = readResponseBody(responseWrapper);
                responseBody = truncate(responseBody, maxBodyLength);
                responseBody = maskBodyHeuristically(responseBody);
            }

            log.info(
                    "[{}] status={} elapsed={}ms resBody={}",
                    traceId,
                    responseWrapper.getStatus(),
                    elapsed,
                    emptyToDash(responseBody)
            );

            responseWrapper.copyBodyToResponse();
        }
    }

    private boolean isBodyReadable(HttpServletRequest request) {
        // GET/DELETE는 바디 없는 경우가 많음(있어도 드묾)
        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) return false;

        String ct = request.getContentType();
        if (ct == null) return false;

        ct = ct.toLowerCase(Locale.ROOT);
        // json / text / xml 정도만 허용(필요 시 확장)
        return ct.contains("application/json") || ct.contains("text/") || ct.contains("application/xml");
    }

    private boolean isResponseBodyReadable(HttpServletResponse response) {
        String ct = response.getContentType();
        if (ct == null) return false;
        ct = ct.toLowerCase(Locale.ROOT);
        return ct.contains("application/json") || ct.contains("text/") || ct.contains("application/xml");
    }

    private String readRequestBody(ContentCachingRequestWrapper req) throws IOException {
        byte[] buf = req.getContentAsByteArray();

        // ContentCachingRequestWrapper는 "읽힌 이후"에 캐시가 채워집니다.
        // 일반적인 @RequestBody 처리 후에는 값이 들어옵니다.
        if (buf == null || buf.length == 0) return null;

        Charset cs = getCharsetOrUtf8(req.getCharacterEncoding());
        return new String(buf, cs);
    }

    private String readResponseBody(ContentCachingResponseWrapper res) throws IOException {
        byte[] buf = res.getContentAsByteArray();
        if (buf == null || buf.length == 0) return null;
        Charset cs = getCharsetOrUtf8(res.getCharacterEncoding());
        return new String(buf, cs);
    }

    private Charset getCharsetOrUtf8(String encoding) {
        try {
            return (encoding != null ? Charset.forName(encoding) : StandardCharsets.UTF_8);
        } catch (Exception e) {
            return StandardCharsets.UTF_8;
        }
    }

    private String buildMaskedParamString(Map<String, String[]> paramMap) {
        if (paramMap == null || paramMap.isEmpty()) return null;

        Map<String, Object> masked = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> e : paramMap.entrySet()) {
            String key = e.getKey();
            String lower = key != null ? key.toLowerCase(Locale.ROOT) : "";

            String[] values = e.getValue();
            if (values == null) {
                masked.put(key, null);
            } else if (values.length == 1) {
                masked.put(key, values[0]);
            } else {
                masked.put(key, Arrays.asList(values));
            }
        }
        return masked.toString();
    }

    private String truncate(String s, int max) {
        if (s == null) return null;
        if (max <= 0) return s;
        if (s.length() <= max) return s;
        return s.substring(0, max) + "...(truncated)";
    }

    private String maskBodyHeuristically(String body) {
        if (body == null) return null;

        String masked = body;
        // 매우 단순한 토큰/패스워드 마스킹 (필요 시 규칙 강화)
        masked = masked.replaceAll("(?i)(\"authorization\"\\s*:\\s*\")[^\"]+\"", "$1****\"");
        masked = masked.replaceAll("(?i)(\"access_token\"\\s*:\\s*\")[^\"]+\"", "$1****\"");
        masked = masked.replaceAll("(?i)(\"refresh_token\"\\s*:\\s*\")[^\"]+\"", "$1****\"");
        masked = masked.replaceAll("(?i)(\"password\"\\s*:\\s*\")[^\"]+\"", "$1****\"");
        return masked;
    }

    private String emptyToDash(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }
}
