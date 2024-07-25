package com.bluemingo.utility;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bluemingo.model.GradeDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GradeDetailsFileReaderUtility {

	public static List<GradeDetail> readGradeDetailsFromFile(String filePath) throws IOException {
		log.info("Inside the readGradeDetailsFromFile");

		List<GradeDetail> gradeDetails = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split("\\s+");
				if (data.length == 7) { 
					// Ensure there are exactly seven elements in the line
					GradeDetail gradeDetail = GradeDetail.fromStringArray(data);
					gradeDetails.add(gradeDetail);
				} 
				else {
					System.err.println("Invalid data format in line: " + line);
				}
			}
		}

		return gradeDetails;

	}
}

