package com.solution;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

@Controller
public class RewardsController {
	
	@RequestMapping(value = "/rewards", method = RequestMethod.POST, 
            consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<String> getRewards(@RequestBody String request) {
		JSONArray custTransactionArr = new JSONArray(request);
		JSONArray rewardsArr = new JSONArray();
		for(Object custTransaction : custTransactionArr) {
			rewardsArr.put(calculateRewards((JSONObject) custTransaction));
		}
		return ResponseEntity.ok().body(rewardsArr.toString());
	}
	
	private JSONObject calculateRewards(JSONObject custTransaction) {
		String customer = (String) custTransaction.get("customer");
		JSONArray transactions = (JSONArray) custTransaction.get("transactions");
		
		HashMap<String, Integer> monthPoints = new HashMap<>();
		int totalPoints = 0;
		System.out.println(customer + transactions);
		
		for (Object transaction : transactions) {
			String date = (String) ((JSONObject) transaction).get("date");
			String price = (String) ((JSONObject) transaction).get("price");
			String month = getMonth(date);
			int points = calculatePoints(price);
			if(monthPoints.containsKey(month))
				monthPoints.put(month, monthPoints.get(month) + points);
			else
				monthPoints.put(month, points);
			
			totalPoints += points;
			System.out.println(month + points);
		}
		return null;
	}
	
	private String getMonth(String date) {
		SimpleDateFormat in = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat out = new SimpleDateFormat("MM");
		Date inDate;
		try {
			inDate = in.parse(date);
			return out.format(inDate);
		} catch (Exception e) {
			return "UNKNOWN";
		}
	}
	
	private int calculatePoints(String price) {
		// Remove '$'
		String formattedPrice = price.substring(1);
		int x;
		try {
			x = Integer.parseInt(formattedPrice);
		} catch (Exception e) {
			return 0;
		}

		if (x >= 100) {
			return 2 * (x - 100) + 50;
		} else if (x >= 50) {
			return (x - 50);
		} else {
			return 0;
		}
	}
}