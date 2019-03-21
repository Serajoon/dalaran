package com.serajoon.dalaran.support.importexport.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseEntity implements Serializable {

    /**
     * 课程名称
     */
    @Excel(name = "课程名称", width = 25, needMerge = true)
    private String name;

    /**
     * 老师主键
     */
    @ExcelEntity
    private TeacherEntity mathTeacher;

    @ExcelCollection(name = "学生", orderNum = "3")
    private List<StudentEntity> students;
}