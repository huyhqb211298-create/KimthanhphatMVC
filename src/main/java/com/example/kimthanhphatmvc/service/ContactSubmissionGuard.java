package com.example.kimthanhphatmvc.service;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

@Component
public class ContactSubmissionGuard {

    static final Duration MINIMUM_FORM_TIME = Duration.ofSeconds(3);
    static final Duration RATE_LIMIT_WINDOW = Duration.ofMinutes(10);
    static final Duration DUPLICATE_WINDOW = Duration.ofMinutes(5);
    static final int MAX_REQUESTS_PER_WINDOW = 3;

    private final Clock clock;
    private final Map<String, Deque<Instant>> attemptsByClient = new HashMap<>();
    private final Map<String, Instant> recentSubmissions = new HashMap<>();

    public ContactSubmissionGuard() {
        this(Clock.systemUTC());
    }

    ContactSubmissionGuard(Clock clock) {
        this.clock = clock;
    }

    public synchronized Decision evaluate(String clientAddress, long formStartedAt,
                                          String name, String email, String phone,
                                          String subject, String message) {
        Instant now = clock.instant();
        long elapsedMillis = now.toEpochMilli() - formStartedAt;
        if (formStartedAt <= 0 || elapsedMillis < MINIMUM_FORM_TIME.toMillis()) {
            return Decision.TOO_FAST;
        }

        cleanup(now);

        String clientKey = normalizeClientAddress(clientAddress);
        Deque<Instant> attempts = attemptsByClient.computeIfAbsent(clientKey, ignored -> new ArrayDeque<>());
        if (attempts.size() >= MAX_REQUESTS_PER_WINDOW) {
            return Decision.RATE_LIMITED;
        }
        attempts.addLast(now);

        String fingerprint = fingerprint(name, email, phone, subject, message);
        if (recentSubmissions.containsKey(fingerprint)) {
            return Decision.DUPLICATE;
        }
        recentSubmissions.put(fingerprint, now);
        return Decision.ALLOWED;
    }

    private void cleanup(Instant now) {
        Instant attemptCutoff = now.minus(RATE_LIMIT_WINDOW);
        Iterator<Map.Entry<String, Deque<Instant>>> attemptIterator = attemptsByClient.entrySet().iterator();
        while (attemptIterator.hasNext()) {
            Deque<Instant> attempts = attemptIterator.next().getValue();
            while (!attempts.isEmpty() && !attempts.peekFirst().isAfter(attemptCutoff)) {
                attempts.removeFirst();
            }
            if (attempts.isEmpty()) {
                attemptIterator.remove();
            }
        }

        Instant duplicateCutoff = now.minus(DUPLICATE_WINDOW);
        recentSubmissions.entrySet().removeIf(entry -> !entry.getValue().isAfter(duplicateCutoff));
    }

    private String normalizeClientAddress(String clientAddress) {
        if (clientAddress == null || clientAddress.isBlank()) {
            return "unknown";
        }
        return clientAddress.strip().toLowerCase(Locale.ROOT);
    }

    private String fingerprint(String name, String email, String phone, String subject, String message) {
        String normalized = String.join("\n",
                normalize(name), normalize(email), normalize(phone), normalize(subject), normalize(message));
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(normalized.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.strip().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    public enum Decision {
        ALLOWED,
        TOO_FAST,
        RATE_LIMITED,
        DUPLICATE
    }
}
