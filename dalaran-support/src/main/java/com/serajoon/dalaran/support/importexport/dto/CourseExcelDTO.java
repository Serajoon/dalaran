package com.serajoon.dalaran.support.importexport.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseExcelDTO {
    @Excel(name = "课程名称", width = 25)
    private String courseName;

    @Excel(name = "学生姓名", height = 20, width = 30, isImportField = "true_st", groupName = "学生")
    private String name;
    /**
     * 学生性别
     */
    @Excel(name = "学生性别", replace = {"男_1", "女_2"}, suffix = "生", isImportField = "true_st", groupName = "学生")
    private int sex;

    @Excel(name = "出生日期", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", isImportField = "true_st", width = 20, groupName = "学生")
    private Date birthday;

    @Excel(name = "进校日期", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd", groupName = "学生")
    private Date registrationDate;
}
