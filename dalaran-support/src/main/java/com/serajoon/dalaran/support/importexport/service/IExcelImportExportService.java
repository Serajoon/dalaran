package com.serajoon.dalaran.support.importexport.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.exception.excel.ExcelExportException;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IExcelImportExportService extends ExportService {

    /**
     * 根据ExcelExportEntity参数导出Excel文件
     *
     * @param exportParams 导出参数,表格标题属性
     * @param entityList   列Map对象列表
     * @param dataSet      Excel对象数据List
     * @return
     * @author hanmeng1
     * @since 2019/3/21 8:54
     */
    void exportFile(String fileName, HttpServletResponse response, ExportParams exportParams, List<ExcelExportEntity> entityList, Collection<?> dataSet);

    /**
     * 导出多Sheet页Excel文件,Excel格式为HSSF
     *
     * @param dataList 数据List
     * @param fileName 文件名
     * @param response response
     */
    void exportFile(List<Map<String, Object>> dataList, String fileName, HttpServletResponse response);

    /**
     * 单sheet页导出Excel文件
     *
     * @param list      data list
     * @param pojoClass pojo类
     * @param fileName  文件名
     * @param response  response
     */
    void exportFile(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response);

    /**
     * 导出单sheet页Excel
     *
     * @param list         数据List
     * @param exportParams exportParams参数
     * @param pojoClass    pojo类
     * @param fileName     导出文件名
     * @param response     response
     * @param contentType  contentType类型 默认为application/vnd.ms-excel
     */
    void exportFile(List<?> list, ExportParams exportParams, Class<?> pojoClass, String fileName, HttpServletResponse response, String contentType);

    /**
     * 导出多sheet页Excel
     *
     * @param sheetsList Excel数据和格式数据配置信息
     * @param type       Excel导出类型
     * @param fileName   导出文件名
     * @param response   response
     */
    void exportFile(List<Map<String, Object>> sheetsList, ExcelType type, String fileName, HttpServletResponse response);

    <T> List<T> importFile(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass);

    default void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams, String contentType) {
        if (!StringUtils.hasText(contentType)) {
            throw new ExcelExportException("Content-Type内容类型为空!");
        }
        if (!StringUtils.hasText(fileName)) {
            throw new ExcelExportException("导出文件名为空!");
        }
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        if (workbook != null) {
            downLoad(fileName, response, workbook, contentType);
        }
    }

    default Map<String, Object> getDataMap(ExportParams exportParams, Class<?> pojoClass, List<?> list) {
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("title", exportParams);
        dataMap.put("entity", pojoClass);
        dataMap.put("data", list);
        return dataMap;
    }

}
