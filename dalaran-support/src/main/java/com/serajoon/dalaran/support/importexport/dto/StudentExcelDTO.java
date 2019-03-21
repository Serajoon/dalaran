package com.serajoon.dalaran.support.importexport.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentExcelDTO {
    @Excel(name = "课程名称", mergeVertical = true)
    private String className;

    @Excel(name = "代课老师", orderNum = "1", mergeVertical = true)
    private String teacher;

    @ExcelEntity(name = "学生", show = true)
    @Excel(name = "学生姓名", orderNum = "2")
    private String studentName;

    @ExcelEntity(name = "学生", show = true)
    @Excel(name = "学生性别", orderNum = "3")
    private String studentSex;

}
