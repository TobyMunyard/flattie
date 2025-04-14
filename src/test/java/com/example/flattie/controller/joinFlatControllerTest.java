// package com.example.flattie.controller;

// import com.example.flattie.model.AppUser;
// import com.example.flattie.model.Flat;
// import com.example.flattie.service.AppUserService;
// import com.example.flattie.service.FlatService;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.List;
// import java.util.Optional;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

// @WebMvcTest(JoinFlatController.class)
// class JoinFlatControllerTest {

// 	@Autowired
// 	private MockMvc mockMvc;

// 	@MockBean
// 	private FlatService flatService;

// 	@MockBean
// 	private AppUserService appUserService;

// 	private Flat testFlat;
// 	private AppUser mockUser;

// 	@BeforeEach
// 	void setup() {
// 		testFlat = new Flat("JOIN123", "Test Flat", "123 Main St", "Dunedin", "9016", "Sunny flat", 250.00, 4);
// 		// Set an ID so redirection can use it.
// 		testFlat.setId(100L);

// 		mockUser = new AppUser("user", "email", "User Name", "password");
// 		mockUser.setId(1L);
// 	}

// 	@AfterEach
// 	void tearDown() {
// 		// Clear any security context if needed.
// 	}

// 	@Test
// 	void testJoinFlat_WithValidCode_UpdatesUserAndReturnsRedirect() throws Exception {
// 		when(flatService.findByJoinCode("JOIN123")).thenReturn(testFlat);
// 		// When joinFlat is called, simply do nothing (or verify later).
// 		doNothing().when(appUserService).joinFlat(any(AppUser.class), any(Flat.class));

// 		// Use SecurityMockMvcRequestPostProcessors.user() to provide your custom
// 		// principal
// 		mockMvc.perform(post("/joinFlat")
// 				.with(SecurityMockMvcRequestPostProcessors.user(mockUser))
// 				.param("flat_code", "JOIN123"))
// 				.andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
// 				.andExpect(status().is3xxRedirection())
// 				.andExpect(redirectedUrl("/showFlatInfo?flatId=" + testFlat.getId()));

// 		verify(appUserService).joinFlat(mockUser, testFlat);
// 	}

// 	@Test
// 	void testJoinFlat_WithInvalidCode_ShowsError() throws Exception {
// 		when(flatService.findByJoinCode("INVALID")).thenReturn(null);

// 		mockMvc.perform(post("/joinFlat")
// 				.with(SecurityMockMvcRequestPostProcessors.user(mockUser))
// 				.param("flat_code", "INVALID"))
// 				.andDo(print())
// 				// The controller returns the view "joinFlat" with an error attribute when flat
// 				// is null.
// 				.andExpect(status().isOk())
// 				.andExpect(view().name("joinFlat"))
// 				.andExpect(model().attributeExists("error"));

// 		verify(appUserService, never()).joinFlat(any(), any());
// 	}

// 	@Test
// 	void testJoinFlat_WithoutAuthentication_RedirectsToLogin() throws Exception {
// 		// No user is provided in the request.
// 		mockMvc.perform(post("/joinFlat")
// 				.param("flat_code", "JOIN123"))
// 				.andDo(print())
// 				.andExpect(status().is3xxRedirection())
// 				.andExpect(redirectedUrl("redirect:/login"));
// 	}
// }
