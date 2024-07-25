package com.bluemingo.utility;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bluemingo.model.OrderDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderDetailsFileReaderUtility {

	public static List<OrderDetail> readOrderDetailsFromFile(String filePath) throws IOException {
		log.info("Inside the readOrderDetailsFromFile");

		List<OrderDetail> orderDetails = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split("\\s+");
				if (data.length == 6) { 
					// Ensure there are exactly six elements in the line
					OrderDetail orderDetail = OrderDetail.fromStringArray(data);
					orderDetails.add(orderDetail);
				} 
				else {
					System.err.println("Invalid data format in line: " + line);
				}
			}			

			return orderDetails;
		}
	}
}