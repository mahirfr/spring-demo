package com.mahir.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.mahir.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class DemoApplicationTests {

	private final WebApplicationContext context;
	private MockMvc mvc;

	DemoApplicationTests(WebApplicationContext context) {
		this.context = context;
	}

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@Test
	void createUser_userIdToBeNull() {
		User user = new User();
		assertNull(user.getId());
	}

	@Test
	void nonAuthenticatedUserCallUsers_statusToBe403() throws Exception {
		mvc.perform(get("/users")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = {"USER"})
	void authenticatedUserCallsUrlSource_statusToBeOk() throws Exception {
		mvc.perform(get("/")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void adminUrlCallUsers_statusToBeOk() throws Exception {
		mvc.perform(get("/users")).andExpect(status().isOk());
	}

}
