package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.config.SecurityConfig;
import com.example.kimthanhphatmvc.controller.ContactController;
import com.example.kimthanhphatmvc.controller.admin.ContactAdminController;
import com.example.kimthanhphatmvc.service.BrandService;
import com.example.kimthanhphatmvc.service.CategoryService;
import com.example.kimthanhphatmvc.service.ContactRequestService;
import com.example.kimthanhphatmvc.service.ContactSubmissionGuard;
import com.example.kimthanhphatmvc.service.ProductTypeService;
import com.example.kimthanhphatmvc.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(controllers = {ContactController.class, ContactAdminController.class})
@Import(SecurityConfig.class)
class SecurityCsrfTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactRequestService contactRequestService;

    @MockitoBean
    private ContactSubmissionGuard submissionGuard;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private BrandService brandService;

    @MockitoBean
    private ProductTypeService productTypeService;

    @BeforeEach
    void allowNormalContactSubmission() {
        when(submissionGuard.evaluate(any(), anyLong(), any(), any(), any(), any(), any()))
                .thenReturn(ContactSubmissionGuard.Decision.ALLOWED);
    }

    @Test
    void publicContactWithoutCsrfTokenIsRejected() throws Exception {
        mockMvc.perform(validContactRequest())
                .andExpect(status().isForbidden());

        verifyNoInteractions(contactRequestService);
    }

    @Test
    void publicContactWithCsrfTokenIsAccepted() throws Exception {
        mockMvc.perform(validContactRequest().with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact#contact-form"));

        verify(contactRequestService).create(
                "Nguyễn Văn An",
                "an@example.com",
                "0905 123 456",
                "Tư vấn hệ thống PCCC",
                "Tôi cần tư vấn cho nhà xưởng.");
    }

    @Test
    void secureResponsesIncludeBrowserProtectionHeaders() throws Exception {
        mockMvc.perform(get("/contact").secure(true))
                .andExpect(header().string("Strict-Transport-Security", containsString("max-age=31536000")))
                .andExpect(header().string("Referrer-Policy", "strict-origin-when-cross-origin"))
                .andExpect(header().string("Permissions-Policy", "camera=(), microphone=(), geolocation=()"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeleteWithoutCsrfTokenIsRejected() throws Exception {
        mockMvc.perform(post("/admin/contacts/2/delete"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(contactRequestService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeleteWithCsrfTokenIsAccepted() throws Exception {
        when(contactRequestService.delete(2L)).thenReturn(true);

        mockMvc.perform(post("/admin/contacts/2/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/contacts"));

        verify(contactRequestService).delete(2L);
    }

    private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder validContactRequest() {
        return post("/contact/submit")
                .param("name", "Nguyễn Văn An")
                .param("email", "an@example.com")
                .param("phone", "0905 123 456")
                .param("subject", "Tư vấn hệ thống PCCC")
                .param("message", "Tôi cần tư vấn cho nhà xưởng.")
                .param("formStartedAt", "1");
    }
}
