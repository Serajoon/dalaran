package com.serajoon.dalaran.support.importexport.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * PersonExcelDTO 必须要有默认的无参数的构造方法
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonExcelDTO  implements Serializable {
    /**
     * name:导出的excel列明
     * replace:值得替换 导出是->{a_id,b_id} 导入反过来<-
     * width:列宽
     * importFormat:导入的时间格式,以这个是否为空来判断是否需要格式化日期
     * exportFormat:导出的时间格式,以这个是否为空来判断是否需要格式化日期
     * orderNum:列的排序 列的排序
     */
    @Excel(name = "姓名")
    private String name;

    /**
     * 导入的时候如果sex是男,则用1替换男 ->
     * 导出时如果查询的数据是1,则用男替换 <-
     */
    @Excel(name = "性别(男/女)", replace = {"男_1", "女_2"}, orderNum = "1", width = 20)
    private String sex;

    @Excel(name = "生日", exportFormat = "yyyy-MM-dd", orderNum = "2", importFormat = "yyyy-MM-dd", width = 20)
    private Date birthday;
}