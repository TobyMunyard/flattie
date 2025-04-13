
package com.example.flattie.controller;

import com.example.flattie.config.TestSecurityConfig;
import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.FlatExpense;
import com.example.flattie.repository.FlatExpenseRepository;
import com.example.flattie.service.FlatExpenseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@SpringBootTest
public class FlatExpenseControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private FlatExpenseService flatExpenseService;

    @MockBean
    private FlatExpenseRepository flatExpenseRepository;

    @Autowired
    private FlatExpenseController flatExpenseController;

    private AppUser mockUser;
    private Flat mockFlat;

    @BeforeEach
    public void setup() {
        mockFlat = new Flat();
        mockFlat.setId(1L);

        mockUser = new AppUser();
        mockUser.setId(99L);
        mockUser.setFlat(mockFlat);

        HandlerMethodArgumentResolver authPrincipalResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(org.springframework.core.MethodParameter parameter) {
                return parameter.getParameterAnnotation(AuthenticationPrincipal.class) != null;
            }

            @Override
            public Object resolveArgument(org.springframework.core.MethodParameter parameter,
                                          ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest,
                                          WebDataBinderFactory binderFactory) {
                return mockUser;
            }
        };

        mockMvc = MockMvcBuilders.standaloneSetup(flatExpenseController)
                .setCustomArgumentResolvers(authPrincipalResolver)
                .build();
    }

    @Test
    public void testSetFlatExpenseDelegations_ValidInput_Success() throws Exception {
        // Arrange
        Long expenseId = 5L;

        String jsonDelegations = """
                [
                  { "flatmate": { "id": 99 }, "amount": 600 },
                  { "flatmate": { "id": 99 }, "amount": 600 }
                ]
                """;

        FlatExpense responseExpense = new FlatExpense();
        responseExpense.setId(expenseId);
        responseExpense.setTotalAmount(BigDecimal.valueOf(1200));

        when(flatExpenseService.saveDelegations(eq(mockUser), eq(expenseId), any()))
                .thenReturn(responseExpense);

        // Act & Assert
        mockMvc.perform(post("/api/flat/expense/delegations")
                .param("expenseId", String.valueOf(expenseId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDelegations))
                .andExpect(status().isOk())
                .andExpect(content().string("Delegations saved successfully."));

        verify(flatExpenseService, times(1))
                .saveDelegations(eq(mockUser), eq(expenseId), any());
    }
}
