package com.bluemingo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GradeDetail {
    private String grade;
    private String gradeGrp;
    private String gradeGrp1;
    private String vdType;
    private String gradeType;
    private String rollingMill;
    private String scrappingGroup;

    @Override
    public String toString() {
        return "GradeDetails{" +
                "grade='" + grade + '\'' +
                ", gradeGrp='" + gradeGrp + '\'' +
                ", gradeGrp1='" + gradeGrp1 + '\'' +
                ", vdType='" + vdType + '\'' +
                ", gradeType='" + gradeType + '\'' +
                ", rollingMill='" + rollingMill + '\'' +
                ", scrappingGroup='" + scrappingGroup + '\'' +
                '}';
    }
    
 // Static method to create an GradeDetail object from string array
 	public static GradeDetail fromStringArray(String[] data) {
 		 String grade = data[0];
 	     String gradeGrp=data[1];
 	     String gradeGrp1=data[2];
 	     String vdType=data[3];
 	     String gradeType=data[4];
 	     String rollingMill=data[5];
 	     String scrappingGroup=data[6];
 		return new GradeDetail( grade,  gradeGrp,  gradeGrp1,  vdType,  gradeType,rollingMill,  scrappingGroup);
 	}

}
