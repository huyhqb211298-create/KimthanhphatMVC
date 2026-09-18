package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.controller.ContactController;
import com.example.kimthanhphatmvc.service.ContactRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContactControllerTests {

    private ContactRequestService contactRequestService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        contactRequestService = mock(ContactRequestService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ContactController(contactRequestService))
                .build();
    }

    @Test
    void validContactIsSavedAndRedirectedWithConfirmation() throws Exception {
        mockMvc.perform(post("/contact/submit")
                        .param("name", "Nguyễn Văn An")
                        .param("email", "an@example.com")
                        .param("phone", "0905 123 456")
                        .param("subject", "Tư vấn hệ thống PCCC")
                        .param("message", "Tôi cần tư vấn cho nhà xưởng."))
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
