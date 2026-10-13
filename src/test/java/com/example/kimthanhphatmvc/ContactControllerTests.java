package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.controller.ContactController;
import com.example.kimthanhphatmvc.service.ContactRequestService;
import com.example.kimthanhphatmvc.service.ContactSubmissionGuard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContactControllerTests {

    private ContactRequestService contactRequestService;
    private ContactSubmissionGuard submissionGuard;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        contactRequestService = mock(ContactRequestService.class);
        submissionGuard = mock(ContactSubmissionGuard.class);
        when(submissionGuard.evaluate(any(), anyLong(), any(), any(), any(), any(), any()))
                .thenReturn(ContactSubmissionGuard.Decision.ALLOWED);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ContactController(contactRequestService, submissionGuard))
                .build();
    }

    @Test
    void validContactIsSavedAndRedirectedWithConfirmation() throws Exception {
        mockMvc.perform(post("/contact/submit")
                        .param("name", "Nguyễn Văn An")
                        .param("email", "an@example.com")
                        .param("phone", "0905 123 456")
                        .param("subject", "Tư vấn hệ thống PCCC")
                        .param("message", "Tôi cần tư vấn cho nhà xưởng.")
                        .param("formStartedAt", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact#contact-form"))
                .andExpect(flash().attributeExists("contactSuccess"));

        verify(contactRequestService).create(
                "Nguyễn Văn An",
                "an@example.com",
                "0905 123 456",
                "Tư vấn hệ thống PCCC",
                "Tôi cần tư vấn cho nhà xưởng.");
    }

    @Test
    void rateLimitedContactIsNotSaved() throws Exception {
        when(submissionGuard.evaluate(any(), anyLong(), any(), any(), any(), any(), any()))
                .thenReturn(ContactSubmissionGuard.Decision.RATE_LIMITED);

        mockMvc.perform(post("/contact/submit")
                        .param("name", "Nguyễn Văn An")
                        .param("email", "an@example.com")
                        .param("phone", "0905 123 456")
                        .param("subject", "Tư vấn hệ thống PCCC")
                        .param("message", "Tôi cần tư vấn cho nhà xưởng.")
                        .param("formStartedAt", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact#contact-form"))
                .andExpect(flash().attributeExists("contactError"));

        verifyNoInteractions(contactRequestService);
    }

    @Test
    void invalidContactIsRejectedWithoutSaving() throws Exception {
        mockMvc.perform(post("/contact/submit")
                        .param("name", "A")
                        .param("email", "khong-hop-le")
                        .param("phone", "123")
                        .param("message", "ngắn"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact#contact-form"))
                .andExpect(flash().attributeExists("contactError"));

        verifyNoInteractions(contactRequestService);
    }
}
