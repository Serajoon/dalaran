package com.serajoon.dalaran.support.importexport.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.serajoon.dalaran.support.importexport.service.IExcelImportExportService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 导入,导出,下载Excel
 */
@Service
public class ExcelImportExportServiceImpl implements IExcelImportExportService {

    private static final String SHEETNAME = "Sheet1";

    private static final String CONTENTTYPE = "application/vnd.ms-excel";

    @Override
    public void exportFile(String fileName, HttpServletResponse response, ExportParams exportParams, List<ExcelExportEntity> entityList, Collection<?> dataList) {
        Optional.of(ExcelExportUtil.exportExcel(exportParams, entityList, dataList)).ifPresent(t -> downLoad(fileName, response, t, CONTENTTYPE));
    }

    @Override
    public void exportFile(List<Map<String, Object>> sheetsList, String fileName, HttpServletResponse response) {
        Optional.ofNullable(ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF)).ifPresent(t -> downLoad(fileName, response, t, CONTENTTYPE));
    }

    @Override
    public void exportFile(List<Map<String, Object>> sheetsList, ExcelType type, String fileName, HttpServletResponse response) {
        Optional.ofNullable(ExcelExportUtil.exportExcel(sheetsList, type)).ifPresent(t -> downLoad(fileName, response, t, CONTENTTYPE));
    }

    @Override
    public void exportFile(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response) {
        exportFile(list, null, pojoClass, fileName, response, null);
    }

    @Override
    public void exportFile(List<?> list, ExportParams exportParams, Class<?> pojoClass, String fileName, HttpServletResponse response, String contentType) {
        if (!StringUtils.hasText(contentType)) {
            contentType = CONTENTTYPE;
        }
        if (exportParams == null) {
            exportParams = new ExportParams();
            exportParams.setSheetName(SHEETNAME);
        }
        exportParams.setType(ExcelType.HSSF);
        defaultExport(list, pojoClass, fileName, response, exportParams, contentType);
    }

    @Override
    public <T> List<T> importFile(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("excel文件不能为空");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }
}
