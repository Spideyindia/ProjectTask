package com.bluemingo.model;

import lombok.Data;

@Data
public class GradeMix {

	private String order_grade;
	private String mixing_possible;

	public GradeMix(String order_grade, String mixing_possible) {
		this.order_grade = order_grade;
		this.mixing_possible = mixing_possible;
	}

	// Static method to create an GradeMixDetail object from string array
	public static GradeMix fromStringArray(String[] data) {
		String order_grade = data[0];
		String mixing_possible=data[1];
		return new GradeMix(order_grade,mixing_possible);
	}

}
