package com.example.demo.api;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.entity.LocationData;
import com.example.demo.entity.LocationDataWrapper;

@SpringBootTest
public class ChatGPTTest {

	@Autowired
	private ChatGPTApiClient chatGPT;

	@Test
	void test_structureLocation() {
		long startTime = System.currentTimeMillis();
		LocationDataWrapper response = chatGPT.structureLocation("第三東京市");
		List<LocationData> location = response.getLocations();
		//		String note=response.getNote();
		System.out.println(location.toString());
		//		System.out.println(note);
		assertThat(location).isNotEmpty();
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("実行時間:" + elapsedTime + "milliseconds");
	}

	@Test
	void test_translateAlert() {
		String alert = """
				    "alert": [
				        {
				            "headline": "Baiyun District Meteorological Observatory issues blue warning for strong winds [IV/General]",
				            "msgtype": "Alert",
				            "severity": "Minor",
				            "urgency": "Unknown",
				            "areas": "Baiyun District",
				            "category": "Met",
				            "certainty": "Unknown",
				            "event": "gale",
				            "note": "",
				            "effective": "2025-09-11T11:05:09+08:00",
				            "expires": "",
				            "desc": "At 11:05 am on September 11, 2025, the Baiyun District Meteorological Observatory issued a blue warning signal for strong winds: it is expected that in the next 24 hours, towns (streets) such as Maijia, Yanshanhong, Shawen, Dula, and Niuchang in our district will be affected by strong winds, with an average wind force of 6 or above and gusts of 7 or above. Please pay attention to checking doors and windows, properly placing outdoor flower pots, etc. When going out, try not to linger near objects that are easily blown by strong winds; Please refer to the meteorological disaster prevention guidelines for relevant units and personnel to do a good job in disaster prevention and reduction.",
				            "instruction": ""
				        }
				        ]
				""";
		long startTime = System.currentTimeMillis();
		String response = chatGPT.translateAlert(alert);
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("実行時間:" + elapsedTime + "milliseconds");
		System.out.println(response);
	}
}
