package com.bluemingo.utility;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bluemingo.model.GradeMix;
import com.bluemingo.model.OrderDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GradeMixFileReaderUtility {

	public static List<GradeMix> readGradeMixDetailsFromFile(String filePath) throws IOException {
		log.info("Inside the readGradeMixDetailsFromFile");

		List<GradeMix> gradeMixDetails = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split("\\s+");
				if (data.length == 2) { 
					// Ensure there are exactly two elements in the line
					GradeMix gradeMixDetail = GradeMix.fromStringArray(data);
					gradeMixDetails.add(gradeMixDetail);
				} 
				else {
					System.err.println("Invalid data format in line: " + line);
				}
			}			

			return gradeMixDetails;
		}
	}
}