package com.solution;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RewardsControllerTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Test
	public void shouldGetUsers() throws Exception {
		// example json
		String request = "[{\"customer\":\"test1\",\"transactions\":[{\"date\":\"07/17/2019\",\"price\":\"$50\"},{\"date\":\"07/18/2019\",\"price\":\"$60\"},{\"date\":\"07/19/2019\",\"price\":\"$90\"},{\"date\":\"08/01/2019\",\"price\":\"$110\"},{\"date\":\"09/17/2019\",\"price\":\"$120\"}]},{\"customer\":\"test2\",\"transactions\":[{\"date\":\"07/17/2019\",\"price\":\"$49\"},{\"date\":\"09/17/2019\",\"price\":\"$120\"}]},{\"customer\":\"test3\",\"transactions\":[{\"date\":\"08/17/2019\",\"price\":\"$1000000\"},{\"date\":\"09/17/2019\",\"price\":\"$120\"}]}]";

		// rewards()
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MvcResult result = mvc.perform(post("/rewards").content(request).contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andReturn();

		// verify data
		String jsonExpectedStr = "[{\"total\":210,\"rewards\":[{\"month\":\"07\",\"points\":50},{\"month\":\"08\",\"points\":70},{\"month\":\"09\",\"points\":90}],\"customer\":\"test1\"},{\"total\":90,\"rewards\":[{\"month\":\"07\",\"points\":0},{\"month\":\"09\",\"points\":90}],\"customer\":\"test2\"},{\"total\":1999940,\"rewards\":[{\"month\":\"08\",\"points\":1999850},{\"month\":\"09\",\"points\":90}],\"customer\":\"test3\"}]";
		JSONArray jsonArrExpected = new JSONArray(jsonExpectedStr);
		String jsonActualStr = result.getResponse().getContentAsString();
		JSONArray jsonArrActual = new JSONArray(jsonActualStr);
		JSONAssert.assertEquals(jsonArrExpected, jsonArrActual, JSONCompareMode.LENIENT);
	}

}
