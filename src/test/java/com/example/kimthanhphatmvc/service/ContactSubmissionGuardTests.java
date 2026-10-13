package com.example.kimthanhphatmvc.service;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class ContactSubmissionGuardTests {

    private static final Instant NOW = Instant.parse("2026-10-13T07:00:00Z");

    @Test
    void rejectsFormSubmittedTooQuickly() {
        ContactSubmissionGuard guard = new ContactSubmissionGuard(Clock.fixed(NOW, ZoneOffset.UTC));

        ContactSubmissionGuard.Decision decision = guard.evaluate(
                "203.0.113.10", NOW.minusSeconds(1).toEpochMilli(),
                "Nguyễn Văn An", "an@example.com", "0905123456", "Tư vấn", "Nội dung cần tư vấn");

        assertThat(decision).isEqualTo(ContactSubmissionGuard.Decision.TOO_FAST);
    }

    @Test
    void rejectsDuplicateSubmissionWithinFiveMinutes() {
        ContactSubmissionGuard guard = new ContactSubmissionGuard(Clock.fixed(NOW, ZoneOffset.UTC));
        long startedAt = NOW.minusSeconds(10).toEpochMilli();

        assertThat(submit(guard, "203.0.113.10", startedAt, "Nội dung cần tư vấn"))
                .isEqualTo(ContactSubmissionGuard.Decision.ALLOWED);
        assertThat(submit(guard, "203.0.113.10", startedAt, "Nội dung cần tư vấn"))
                .isEqualTo(ContactSubmissionGuard.Decision.DUPLICATE);
    }

    @Test
    void limitsEachClientToThreeSubmissionsPerTenMinutes() {
        ContactSubmissionGuard guard = new ContactSubmissionGuard(Clock.fixed(NOW, ZoneOffset.UTC));
        long startedAt = NOW.minusSeconds(10).toEpochMilli();

        assertThat(submit(guard, "203.0.113.10", startedAt, "Yêu cầu số một"))
                .isEqualTo(ContactSubmissionGuard.Decision.ALLOWED);
        assertThat(submit(guard, "203.0.113.10", startedAt, "Yêu cầu số hai"))
                .isEqualTo(ContactSubmissionGuard.Decision.ALLOWED);
        assertThat(submit(guard, "203.0.113.10", startedAt, "Yêu cầu số ba"))
                .isEqualTo(ContactSubmissionGuard.Decision.ALLOWED);
        assertThat(submit(guard, "203.0.113.10", startedAt, "Yêu cầu số bốn"))
                .isEqualTo(ContactSubmissionGuard.Decision.RATE_LIMITED);
    }

    @Test
    void acceptsNewSubmissionAfterRateLimitWindowExpires() {
        MutableClock clock = new MutableClock(NOW);
        ContactSubmissionGuard guard = new ContactSubmissionGuard(clock);
        long startedAt = NOW.minusSeconds(10).toEpochMilli();

        submit(guard, "203.0.113.10", startedAt, "Yêu cầu số một");
        submit(guard, "203.0.113.10", startedAt, "Yêu cầu số hai");
        submit(guard, "203.0.113.10", startedAt, "Yêu cầu số ba");
        clock.advanceSeconds(601);

        assertThat(submit(guard, "203.0.113.10", startedAt, "Yêu cầu số bốn"))
                .isEqualTo(ContactSubmissionGuard.Decision.ALLOWED);
    }

    private ContactSubmissionGuard.Decision submit(ContactSubmissionGuard guard, String address,
                                                     long startedAt, String message) {
        return guard.evaluate(address, startedAt,
                "Nguyễn Văn An", "an@example.com", "0905123456", "Tư vấn", message);
    }

    private static final class MutableClock extends Clock {
        private Instant instant;

        private MutableClock(Instant instant) {
            this.instant = instant;
        }

        private void advanceSeconds(long seconds) {
            instant = instant.plusSeconds(seconds);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
