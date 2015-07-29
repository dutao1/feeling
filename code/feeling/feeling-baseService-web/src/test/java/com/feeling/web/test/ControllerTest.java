package com.feeling.web.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextHierarchy(@ContextConfiguration(locations = { "classpath*:/spring/spring-root-test.xml"}))
public class ControllerTest {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	private Logger logger= LoggerFactory.getLogger(ControllerTest.class);
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	@Test
	public void testReg() throws Exception {
		MockHttpServletRequestBuilder httpRequest = MockMvcRequestBuilders
				//&citycode=asdfasf&discode=asasdfasfd&merchantcode=1
				.get("/reg?mobile=123");

		MvcResult result = mockMvc.perform(httpRequest)
				.andDo(MockMvcResultHandlers.print())
				.andReturn();

		System.out.println(result);
	}
	
}
