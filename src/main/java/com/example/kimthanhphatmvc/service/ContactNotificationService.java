package com.example.kimthanhphatmvc.service;

import com.example.kimthanhphatmvc.model.ContactRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ContactNotificationService {

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final boolean enabled;
    private final String recipient;
    private final String from;

    public ContactNotificationService(
            ObjectProvider<JavaMailSender> mailSenderProvider,
            @Value("${contact.notification.enabled:false}") boolean enabled,
            @Value("${contact.notification.email:info@kimthanhphatpccc.vn}") String recipient,
            @Value("${contact.notification.from:}") String from) {
        this.mailSenderProvider = mailSenderProvider;
        this.enabled = enabled;
        this.recipient = recipient;
        this.from = from;
    }

    public NotificationResult send(ContactRequest request) {
        if (!enabled) {
            return new NotificationResult(false, "Thông báo email chưa được bật");
        }

        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            return new NotificationResult(false, "SMTP chưa được cấu hình");
        }

        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(recipient);
            if (StringUtils.hasText(from)) {
                email.setFrom(from);
            }
            email.setReplyTo(request.getEmail());
            email.setSubject("[Website] Yêu cầu liên hệ mới từ " + request.getName());
            email.setText("""
                    Có yêu cầu liên hệ mới từ website Kim Thành Phát.

                    Họ và tên: %s
                    Số điện thoại: %s
                    Email: %s
                    Chủ đề: %s

                    Nội dung:
                    %s

                    Mã yêu cầu: #%d
                    """.formatted(
                    request.getName(),
                    request.getPhone(),
                    request.getEmail(),
                    StringUtils.hasText(request.getSubject()) ? request.getSubject() : "Không có",
                    request.getMessage(),
                    request.getId()));
            mailSender.send(email);
            return new NotificationResult(true, null);
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            return new NotificationResult(false,
                    StringUtils.hasText(message) ? message : exception.getClass().getSimpleName());
        }
    }

    public record NotificationResult(boolean sent, String error) {
    }
}
