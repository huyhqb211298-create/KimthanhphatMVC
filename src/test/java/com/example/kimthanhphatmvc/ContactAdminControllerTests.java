package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.controller.admin.ContactAdminController;
import com.example.kimthanhphatmvc.service.ContactRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContactAdminControllerTests {

    private ContactRequestService contactRequestService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        contactRequestService = mock(ContactRequestService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ContactAdminController(contactRequestService))
                .build();
    }

    @Test
    void deleteRemovesContactAndReturnsConfirmation() throws Exception {
        when(contactRequestService.delete(2L)).thenReturn(true);

        mockMvc.perform(post("/admin/contacts/2/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/contacts"))
                .andExpect(flash().attributeExists("message"));

        verify(contactRequestService).delete(2L);
    }
}
