package com.serajoon.dalaran.support.importexport.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@ExcelTarget("teacherEntity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherEntity implements java.io.Serializable {
//    private String id;
    /**
     * name
     */
    //@Excel(name = "主讲老师_major,代课老师_absent", orderNum = "1", isImportField = "true_major,true_absent")
    @Excel(name = "主讲老师", orderNum = "1", isImportField = "true_major,true_absent", needMerge = true)
    private String name;
}