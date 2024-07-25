package com.bluemingo;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.bluemingo.dao.MySQLDao;
import com.bluemingo.model.GradeDetail;
import com.bluemingo.model.GradeMix;
import com.bluemingo.model.OrderDetail;
import com.bluemingo.utility.GradeDetailsFileReaderUtility;
import com.bluemingo.utility.GradeMixFileReaderUtility;
import com.bluemingo.utility.OrderDetailsFileReaderUtility;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class BluemingoApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(BluemingoApplication.class, args);
		MySQLDao dao = ctx.getBean("mySQLDAO",MySQLDao.class);
		Scanner scn = new Scanner(System.in);

		List<OrderDetail> orderDetails = null;
		List<GradeDetail> gradeDetails = null;
		List<GradeMix> gradeMixDetails = null;

		String orderfilePath = "src/main/java/com/bluemingo/utility/Order_Details.txt";
		String gradefilePath = "src/main/java/com/bluemingo/utility/Grade_Details.txt";
		String grademixfilePath = "src/main/java/com/bluemingo/utility/Grade_Mix.txt";

		try {
			log.info("Read OrderDetails.txt file contents and add to DB? Answer in Yes or No :: ");
			String input1 = scn.nextLine();
			if(input1.equalsIgnoreCase("Yes")) {
				orderDetails = OrderDetailsFileReaderUtility.readOrderDetailsFromFile(orderfilePath);
				dao.addOrderDetailEntries(orderDetails);			//Exptd : 6650 entries
				log.info("OrderDetails.txt file had entries count :: "+String.valueOf(orderDetails.size()));
			}

			log.info("Read Grade_Details.txt file contents and add to DB? Answer in Yes or No :: ");
			String input2 = scn.nextLine();
			if(input2.equalsIgnoreCase("Yes")) {
				gradeDetails = GradeDetailsFileReaderUtility.readGradeDetailsFromFile(gradefilePath);
				dao.addGradeDetailEntries(gradeDetails);			//Exptd : 987 entries
				log.info("Grade_Details.txt file had entries count :: "+String.valueOf(gradeDetails.size()));
			}

			log.info("Read Grade_Mix.txt file contents and add to DB? Answer in Yes or No :: ");
			String input3 = scn.nextLine();
			if(input3.equalsIgnoreCase("Yes")) {
				gradeMixDetails = GradeMixFileReaderUtility.readGradeMixDetailsFromFile(grademixfilePath);
				//dao.addGradeMixEntries(gradeMixDetails);		//Not having PK column so should be called under specific conditions to not add duplicate entries.
				log.info("Grade_Mix.txt file had entries count :: "+String.valueOf(gradeMixDetails.size()));
			}

			log.info("Calling method to get records from specific counts upto L1 group as specified in the given task sheet ::");
			log.info("start count : ");
			Integer input4 = scn.nextInt();
			log.info("end count : ");
			Integer input5 = scn.nextInt();
			dao.fetchUptoL1Group(input4 , input5);

		} catch (IOException e) {
			log.error("System encountered with the error :"+e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.error("System encountered with the error :"+e);
		}


	}//main(-)

}//class
