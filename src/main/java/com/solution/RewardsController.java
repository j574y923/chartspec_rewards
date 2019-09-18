package com.solution;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

@Controller
public class RewardsController {
	
	@RequestMapping(value = "/rewards", method = RequestMethod.POST, 
            consumes = "application/json", produces = "application/json")
	public @ResponseBody String getRewards(@RequestBody String request) {
		
		return "TEST" + request;
	}
}