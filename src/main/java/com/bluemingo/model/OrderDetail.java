package com.bluemingo.model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import lombok.Data;

@Data	
public class OrderDetail {
	private int orderNo;
	private int orderWidth;
	private String grade;
	private LocalDate deliveryDate;
	private int btrQty;
	private String product;

	// Constructor
	public OrderDetail(int orderNo, int orderWidth, String grade, LocalDate deliveryDate, int btrQty, String product) {
		this.orderNo = orderNo;
		this.orderWidth = orderWidth;
		this.grade = grade;
		this.deliveryDate = deliveryDate;
		this.btrQty = btrQty;
		this.product = product;
	}

	// Static method to create an OrderDetail object from string array
	public static OrderDetail fromStringArray(String[] data) {
		int orderNo = Integer.parseInt(data[0]);
		int orderWidth = Integer.parseInt(data[1]);
		String grade = data[2];
		LocalDate deliveryDate;
	try {	deliveryDate = LocalDate.parse(data[3],new DateTimeFormatterBuilder()
			    .parseCaseInsensitive()
			    .appendPattern("dd-MMM-yyyy")
			    .toFormatter(Locale.ENGLISH));
	}catch (Exception e) {
		deliveryDate = LocalDate.parse(data[3],new DateTimeFormatterBuilder()
			    .parseCaseInsensitive()
			    .appendPattern("dd-MM-yyyy")
			    .toFormatter(Locale.ENGLISH));
	}
		int btrQty = Integer.parseInt(data[4]);
		String product = data[5];

		return new OrderDetail(orderNo, orderWidth, grade, deliveryDate, btrQty, product);
	}
}



