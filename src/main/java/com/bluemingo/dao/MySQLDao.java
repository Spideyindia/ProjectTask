package com.bluemingo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bluemingo.model.GradeDetail;
import com.bluemingo.model.GradeMix;
import com.bluemingo.model.OrderDetail;

import lombok.extern.slf4j.Slf4j;

@Repository("mySQLDAO")
@Slf4j
public class MySQLDao{

	@Autowired
	private DataSource ds;

	public void addGradeMixEntries(List<GradeMix> gradeMixDetails) {
		//		log.info(ds.getClass());	//Just to validate DB Connection
		log.info("From addGradeMixEntries");		//To check in logs for this method call
		try(Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(INSERT_GRADE_MIX_DETAILS);){
			int count =0;
			for(GradeMix gradeMix : gradeMixDetails) {
				int insertCount=0;
				try {
					ps.setString(1, gradeMix.getOrder_grade());
					ps.setString(2, gradeMix.getMixing_possible());
					ps.executeUpdate();
					count++;
				}catch(SQLIntegrityConstraintViolationException sie) {
					insertCount++;
					continue;
				}
			}
			log.info("Number of records added are : "+count);
		}catch(SQLIntegrityConstraintViolationException sie) {
			log.error("This record is already present: ");
		}catch (SQLException se) {
			log.error(se.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	public void addGradeDetailEntries(List<GradeDetail> gradeDetails) {
		//		log.info(ds.getClass());	//Just to validate DB Connection
		log.info("From addGradeDetailEntries");		//To check in logs for this method call
		try(Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(INSERT_GRADE_DETAILS);){
			int count =0;
			for(GradeDetail gradeDetail : gradeDetails) {
				int insertCount = 0;
				try {
					ps.setString(1, gradeDetail.getGrade());
					ps.setString(2, gradeDetail.getGradeGrp());
					ps.setString(3, gradeDetail.getGradeGrp1());
					ps.setString(4, gradeDetail.getVdType());
					ps.setString(5, gradeDetail.getGradeType());
					ps.setString(6, gradeDetail.getRollingMill());
					ps.setString(7, gradeDetail.getScrappingGroup());
					ps.executeUpdate();
					count++;
				} catch(SQLIntegrityConstraintViolationException sie) {
					insertCount++;
					continue;
				}
			}
			log.info("Number of records added are : "+count);
		}catch(SQLIntegrityConstraintViolationException sie) {
			log.error("This record is already present: ");
		}catch (SQLException se) {
			log.error(se.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	public void addOrderDetailEntries(List<OrderDetail> orderDetails) {
		//		log.info(ds.getClass());	//Just to validate DB Connection
		log.info("From addOrderDetailEntries");		//To check in logs for this method call
		try(Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(INSERT_ORDER_DETAILS);){
			int count =0;
			for(OrderDetail orderDetail : orderDetails) {
				int insertCount = 0;
				try {
					ps.setInt(1, orderDetail.getOrderNo());
					ps.setInt(2, orderDetail.getOrderWidth());
					ps.setString(3, orderDetail.getGrade());
					ps.setDate(4, java.sql.Date.valueOf(orderDetail.getDeliveryDate()));
					ps.setInt(5, orderDetail.getBtrQty());
					ps.setString(6, orderDetail.getProduct());
					ps.executeUpdate();
					count++;
				} catch(SQLIntegrityConstraintViolationException sie) {
					insertCount++;
					continue;
				}
			}
			log.info("Number of records added are : "+count);

		}catch (SQLException se) {
			log.error(se.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage());
		}
	}//method 3

	public void fetchUptoL1Group(Integer start, Integer end) {
		log.info("From fetchUptoL1Group");		//To check in logs for this method call
		try(Connection con = ds.getConnection();
				PreparedStatement ps1 = con.prepareStatement(SET_BUCKET_SIZE);
				PreparedStatement ps2 = con.prepareStatement(FETCH_RECORDS_USING_TABLES_WITH_L1_GROUP);){
			int count =0;
			//Setting Bucket Size before executing query  -- Customizable Bucket Size 
			boolean validate = ps1.execute();
			log.info("If comes false means the bucket days are set properly :: "+String.valueOf(validate)); 				//must be false as non select query is executed
			try(ResultSet rs = ps2.executeQuery()){

				log.info("Order_No"+" "+"Order_Width"+" "+"New_Set_Width"+" "+"Grade"+" "+"Grade Grp"+" "+"Delivery_Date"+" "+"L1_Group"+" "+"Ceiling_Order_Width"+" "+"Prev_Ceiling_Order_Width"+" "+"Gap"+" "+"Set_Width"+" "+"Set_Width_Plus_15"+" "+"Diff"+" "+"BTR_Qty"+" "+"BTC_Qty"+" "+"Product"+" "+"bucket"+" "+"total_records"+" "+"width_rank");

				while(rs.next()) {
					count++;
					if(count>start && count<=end) {
						log.info(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+" "+rs.getString(5)+" "+rs.getString(6)+" "+rs.getString(7)+" "+rs.getString(8)+" "+rs.getString(9)+" "+rs.getString(10)+" "+rs.getString(11)+" "+rs.getString(12)+" "+rs.getString(13)+" "+rs.getString(14)+" "+rs.getString(15)+" "+rs.getString(16)+" "+rs.getString(17)+" "+rs.getString(18)+" "+rs.getString(19));
					}
				}//while
			}//try1

		}

		catch (SQLException se) {
			se.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}//method 4


	private static final String INSERT_GRADE_MIX_DETAILS = "INSERT INTO GradeMix (`Order Grade`, `Mixing Possible`) VALUES (?,?)";
	private static final String INSERT_GRADE_DETAILS = "INSERT INTO GradeDetails (Grade, `Grade Grp`, `Grade Grp1`, `VD TYPE`, `GRADE TYPE`, `Rolling MILL`, `Scrafing Group`) VALUES (?,?,?,?,?,?,?);";
	private static final String INSERT_ORDER_DETAILS = "INSERT INTO OrderDetail (Order_No, Order_Width, Grade, Delivery_Date, BTR_Qty, Product)VALUES (?,?,?,?,?,?);";
	private static final String SET_BUCKET_SIZE = "SET @bucket_size = 3; -- Set the bucket size here (number of unique delivery dates per bucket)";
	private static final String FETCH_RECORDS_USING_TABLES_WITH_L1_GROUP = "WITH subquery AS (\r\n"
			+ "    SELECT\r\n"
			+ "        od.Order_No,\r\n"
			+ "        od.Order_Width,\r\n"
			+ "        od.Grade,\r\n"
			+ "        od.Delivery_Date,\r\n"
			+ "        od.BTR_Qty,\r\n"
			+ "        od.BTR_Qty * 1.1 AS BTC_Qty, -- Calculate BTC_Qty as BTR_Qty * 1.1\r\n"
			+ "        od.Product,\r\n"
			+ "        gd.`Grade Grp`,\r\n"
			+ "        CONCAT('L1G', DENSE_RANK() OVER (ORDER BY CAST(SUBSTRING_INDEX(gd.`Grade Grp`, 'AG', -1) AS UNSIGNED))) AS L1_Group, -- Assign L1_Group based on numeric part of Grade Grp\r\n"
			+ "        FLOOR((DENSE_RANK() OVER (ORDER BY od.Delivery_Date) - 1) / @bucket_size) AS bucket,\r\n"
			+ "        COUNT(*) OVER () AS total_records,\r\n"
			+ "        DENSE_RANK() OVER (PARTITION BY CAST(SUBSTRING_INDEX(gd.`Grade Grp`, 'AG', -1) AS UNSIGNED) ORDER BY od.Order_Width DESC) AS width_rank,\r\n"
			+ "        CASE\r\n"
			+ "            WHEN od.Order_Width % 25 = 0 THEN od.Order_Width\r\n"
			+ "            ELSE CEILING(od.Order_Width / 25) * 25\r\n"
			+ "        END AS Ceiling_Order_Width -- Calculate Ceiling_Order_Width\r\n"
			+ "    FROM (\r\n"
			+ "        SELECT DISTINCT\r\n"
			+ "            Order_No,\r\n"
			+ "            Order_Width,\r\n"
			+ "            Grade,\r\n"
			+ "            Delivery_Date,\r\n"
			+ "            BTR_Qty,\r\n"
			+ "            Product\r\n"
			+ "        FROM OrderDetail\r\n"
			+ "    ) od\r\n"
			+ "    LEFT JOIN GradeDetails gd ON od.Grade = gd.Grade\r\n"
			+ ")\r\n"
			+ "\r\n"
			+ "SELECT\r\n"
			+ "    final_result.Order_No,\r\n"
			+ "    final_result.Order_Width,\r\n"
			+ "    final_result.New_Set_Width,\r\n"
			+ "    final_result.Grade,\r\n"
			+ "    final_result.`Grade Grp`,\r\n"
			+ "    final_result.Delivery_Date,\r\n"
			+ "    final_result.L1_Group,\r\n"
			+ "    final_result.Ceiling_Order_Width,\r\n"
			+ "    final_result.Prev_Ceiling_Order_Width,\r\n"
			+ "    final_result.Gap,\r\n"
			+ "    final_result.Set_Width,\r\n"
			+ "    CASE\r\n"
			+ "        WHEN final_result.Set_Width IS NOT NULL THEN final_result.Set_Width + 15\r\n"
			+ "        ELSE NULL\r\n"
			+ "    END AS Set_Width_Plus_15,\r\n"
			+ "    final_result.Diff,\r\n"
			+ "    final_result.BTR_Qty,\r\n"
			+ "    final_result.BTC_Qty,\r\n"
			+ "    final_result.Product,\r\n"
			+ "    final_result.bucket,\r\n"
			+ "    final_result.total_records,\r\n"
			+ "    final_result.width_rank\r\n"
			+ "FROM (\r\n"
			+ "    SELECT\r\n"
			+ "        final_query.*,\r\n"
			+ "        CASE\r\n"
			+ "            WHEN final_query.Set_Width IS NOT NULL THEN final_query.Set_Width + 15 - final_query.Order_Width\r\n"
			+ "            ELSE NULL\r\n"
			+ "        END AS Diff,\r\n"
			+ "        CASE\r\n"
			+ "            WHEN final_query.Set_Width IS NULL AND \r\n"
			+ "                 (final_query.Set_Width + 15 - final_query.Order_Width) IS NULL THEN final_query.Ceiling_Order_Width\r\n"
			+ "            ELSE final_query.Set_Width\r\n"
			+ "        END AS New_Set_Width\r\n"
			+ "    FROM (\r\n"
			+ "        SELECT\r\n"
			+ "            subquery.Order_No,\r\n"
			+ "            subquery.Order_Width,\r\n"
			+ "            subquery.Ceiling_Order_Width,\r\n"
			+ "            gd.`Grade Grp`,\r\n"
			+ "            subquery.Grade,\r\n"
			+ "            subquery.L1_Group,\r\n"
			+ "            LAG(subquery.Ceiling_Order_Width) OVER (PARTITION BY subquery.L1_Group ORDER BY subquery.width_rank, subquery.Delivery_Date) AS Prev_Ceiling_Order_Width,\r\n"
			+ "            ABS(subquery.Ceiling_Order_Width - LAG(subquery.Ceiling_Order_Width) OVER (PARTITION BY subquery.L1_Group ORDER BY subquery.width_rank, subquery.Delivery_Date)) AS Gap,\r\n"
			+ "            CASE\r\n"
			+ "                WHEN ABS(subquery.Ceiling_Order_Width - LAG(subquery.Ceiling_Order_Width) OVER (PARTITION BY subquery.L1_Group ORDER BY subquery.width_rank, subquery.Delivery_Date)) = 0 OR\r\n"
			+ "                     ABS(subquery.Ceiling_Order_Width - LAG(subquery.Ceiling_Order_Width) OVER (PARTITION BY subquery.L1_Group ORDER BY subquery.width_rank, subquery.Delivery_Date)) = 25 THEN NULL\r\n"
			+ "                WHEN ABS(subquery.Ceiling_Order_Width - LAG(subquery.Ceiling_Order_Width) OVER (PARTITION BY subquery.L1_Group ORDER BY subquery.width_rank, subquery.Delivery_Date)) = 50 THEN subquery.Ceiling_Order_Width + 25\r\n"
			+ "                ELSE NULL\r\n"
			+ "            END AS Set_Width,\r\n"
			+ "            subquery.Delivery_Date,\r\n"
			+ "            subquery.BTR_Qty,\r\n"
			+ "            subquery.BTC_Qty,\r\n"
			+ "            subquery.Product,\r\n"
			+ "            subquery.bucket,\r\n"
			+ "            subquery.total_records,\r\n"
			+ "            subquery.width_rank\r\n"
			+ "        FROM subquery\r\n"
			+ "        LEFT JOIN GradeDetails gd ON subquery.Grade = gd.Grade\r\n"
			+ "    ) AS final_query\r\n"
			+ ") AS final_result\r\n"
			+ "ORDER BY CAST(SUBSTRING(final_result.L1_Group, 4) AS UNSIGNED), final_result.width_rank, final_result.Delivery_Date;\r\n"
			+ "\r\n"
			+ "\r\n"
			+ "-- BASE QUERY TILL L1_Group";

}