package com.serajoon.dalaran.support.importexport.controller;


import cn.afterturn.easypoi.cache.manager.POICacheManager;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.google.common.collect.Lists;
import com.serajoon.dalaran.common.constants.MyCharset;
import com.serajoon.dalaran.common.web.response.ResponseResult;
import com.serajoon.dalaran.support.importexport.config.ImportExportProperties;
import com.serajoon.dalaran.support.importexport.dto.*;
import com.serajoon.dalaran.support.importexport.service.IExcelImportExportService;
import com.serajoon.dalaran.support.importexport.style.ExcelStyleGray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;


/**
 * # 路径为resource目录下的excel目录
 * importexport:
 * # 导入
 * import-template-map:
 * xxx: files/import/测试.xlsx
 * exampleImport: files/import/导入模板例子.xlsx
 * # 导出
 * export-template-map:
 * exampleExport: 导出例子.xlsx
 */
@RestController
@Slf4j
@Api(description = "Excel导入导出", tags = "ExcelController")
public class ExcelController {

    @Resource
    private ImportExportProperties importExportProperties;

    @Resource
    private IExcelImportExportService importExportService;

    /**
     * 根据application配置文件中配置的信息,获得要导出的文件名,使用注解的方式导出
     * http://localhost:8000/demo/export/singleSheetWithAnnotation/exampleExport
     *
     * @param key      importexport.export-template-map.exampleExport对应的文件名称
     * @param response response
     */
    @GetMapping("/export/singleSheetWithAnnotation/{key}")
    @ApiOperation("导出单sheet简单Excel例子")
    public void exportSingleSheetWithAnnotation(@PathVariable String key, HttpServletResponse response) {
        //获得导出文件名
        String fileName = importExportProperties.getExportTemplateMap().get(key);
        //定义sheet页名称
        String sheetName = "人员信息";
        //模拟从数据库获取需要导出的数据
        List<PersonExcelDTO> personExcelDTOList = getPersonFromDB();
        //导出操作
        importExportService.exportFile(personExcelDTOList, PersonExcelDTO.class, fileName, response);
    }

    /**
     * 导出带有和合并和列的单表头Sheet页
     * http://localhost:8000/demo/export/singleSheetComplexedWithAnnotation/exampleExport
     *
     * @param key      importexport.export-template-map.exampleExport对应的文件名称
     * @param response response
     */
    @GetMapping("/export/singleSheetComplexedWithAnnotation/{key}")
    @ApiOperation("导出单sheet复杂Excel例子")
    public void exportSingleSheetComplexedWithAnnotation(@PathVariable String key, HttpServletResponse response) {
        //获得导出文件名
        String fileName = importExportProperties.getExportTemplateMap().get(key);
        ExportParams exportParams = new ExportParams();
        exportParams.setTitle("学生课程信息表");
        exportParams.setSheetName("测试");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, CourseEntity.class, getCourseFromDB());
        if (workbook != null) {
            importExportService.downLoad(fileName, response, workbook, "application/vnd.ms-excel");
        }
    }

    /**
     * http://localhost:8000/demo/export/multiSheetWithAnnotation/exampleExport
     *
     * @param key
     * @param response
     */
    @GetMapping("/export/multiSheetWithAnnotation/{key}")
    @ApiOperation("导出多sheet复杂Excel例子")
    public void exportmultiSheetWithAnnotation(@PathVariable String key, HttpServletResponse response) {
        //获得导出文件名
        String fileName = importExportProperties.getExportTemplateMap().get(key);
        //模拟从数据库获取需要导出的数据
        //设置导出配置
        // 创建参数对象（用来设定excel的sheet得内容等信息）
        /**
         * sheet1
         */
        ExportParams params1 = new ExportParams();
        // 设置sheet得名称
        params1.setSheetName("王者荣耀");
        params1.setTitle("表格1");
        // 创建sheet1使用得map
        Map<String, Object> dataMap1 = importExportService.getDataMap(params1, PersonExcelDTO.class, getPersonFromDB());
        /**
         * sheet2
         */
        ExportParams params2 = new ExportParams();
        params2.setSheetName("学生课程信息表");
        params2.setTitle("表格2");
        Map<String, Object> dataMap2 = importExportService.getDataMap(params2, StudentExcelDTO.class, getStudentFromDB());
        /**
         * sheet3
         */
        ExportParams params3 = new ExportParams();
        params3.setTitle("学生课程信息表");
        params3.setSheetName("表格3");
        Map<String, Object> dataMap3 = importExportService.getDataMap(params3, CourseEntity.class, getCourseFromDB());

        /**
         * sheet4
         */
        ExportParams params4 = new ExportParams();
        params4.setTitle("学生课程信息表");
        params4.setSheetName("表格4");
        Map<String, Object> dataMap4 = importExportService.getDataMap(params4, CourseExcelDTO.class, getCourseExcelDTOFromDB());

        // 将sheet1,sheet2,sheet3使用得map进行包装
        List<Map<String, Object>> sheetsList = Lists.newArrayList();
        sheetsList.add(dataMap1);
        sheetsList.add(dataMap2);
        sheetsList.add(dataMap3);
        sheetsList.add(dataMap4);
        /**
         * HSSF是POI工程对Excel 97(-2007)文件操作的纯Java实现
         * XSSF是POI工程对Excel 2007 OOXML (.xlsx)文件操作的纯Java实现x
         */
        //导出操作
        importExportService.exportFile(sheetsList, fileName, response);
    }

    // http://localhost:8000/demo/a/exampleExport
    @GetMapping("/export/singleSheetWithoutAnnotationA/{key}")
    public void a(@PathVariable String key, HttpServletResponse response) {
        String fileName = importExportProperties.getExportTemplateMap().get(key);
        List<ExcelExportEntity> colList = new ArrayList<>();
        colList.add(new ExcelExportEntity("姓名", "name"));
        colList.add(new ExcelExportEntity("性别", "sex"));
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> map;
        for (int i = 0; i < 10; i++) {
            map = new HashMap<>();
            map.put("name", "1" + i);
            map.put("sex", "2" + i);
            dataList.add(map);
        }
        ExportParams exportParams = new ExportParams("表", "数据");
        exportParams.setStyle(ExcelStyleGray.class);
        importExportService.exportFile(fileName, response, exportParams, colList, dataList);
    }


    //pojo类必须要有默认构造方法
    // http://localhost:8000/demo/import
    @PostMapping(value = "/import", consumes = "multipart/*", headers = "content-type=multipart/form-data", produces = "application/json; charset=utf-8")
    @ApiOperation("导入例子")
    public void importExcel(@RequestParam("file") MultipartFile multipartFile) {
        //解析Excel
        List<PersonExcelDTO> list = importExportService.importFile(multipartFile, 0, 1, PersonExcelDTO.class);
        list = list.parallelStream().filter(t -> Objects.nonNull(t.getName())).collect(Collectors.toList());
        System.out.println("导入数据一共[" + list.size() + "]行");
        //TODO 保存数据库
    }

    /**
     * http://localhost:8000/em/template/exampleImport
     *
     * @param response
     * @param templateKey 模板对应的key
     * @return
     */
    @GetMapping("/template/{templateKey}")
    @ApiOperation("模板下载")
    public ResponseResult downLoad(HttpServletResponse response, @PathVariable String templateKey) {
        ResponseResult result = null;
        String absolutePath = importExportProperties.getImportTemplateMap().get(templateKey);
        try {
            //获取文件名
            if (StringUtils.hasText(absolutePath)
                    && Objects.nonNull(ClassLoader.getSystemResourceAsStream(absolutePath))) {
                String fileName = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
                InputStream in = POICacheManager.getFile(absolutePath);
                response.setCharacterEncoding(MyCharset.DEFAULT_CHARSET_TEXT);
                response.setContentType("application/octet-stream;charset=" + MyCharset.DEFAULT_CHARSET_TEXT);
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(fileName, MyCharset.DEFAULT_CHARSET_TEXT));
                importExportService.transferFile(in, response);
            } else {
                log.error("清检查模板路径是否正确:参数{}", templateKey);
                throw new FileNotFoundException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = ResponseResult.build().failed("模板文件不存在");
        }
        return result;
    }

    //模拟从数据库查询数据
    private List<PersonExcelDTO> getPersonFromDB() {
        List<PersonExcelDTO> personExcelDTOList = Lists.newArrayList();
        PersonExcelDTO personExcelDTO1 = new PersonExcelDTO("亚瑟", "1", new Date());
        PersonExcelDTO personExcelDTO2 = new PersonExcelDTO("露娜", "2", new Date());
        PersonExcelDTO personExcelDTO3 = new PersonExcelDTO("娜可露露", "2", new Date());
        PersonExcelDTO personExcelDTO4 = new PersonExcelDTO("狄仁杰", "1", new Date());
        personExcelDTOList.add(personExcelDTO1);
        personExcelDTOList.add(personExcelDTO2);
        personExcelDTOList.add(personExcelDTO3);
        personExcelDTOList.add(personExcelDTO4);
        return personExcelDTOList;
    }

    private List<StudentExcelDTO> getStudentFromDB() {
        List<StudentExcelDTO> studentList = Lists.newArrayList();
        studentList.add(new StudentExcelDTO("Java", "老王", "A", "男"));
        studentList.add(new StudentExcelDTO("Java", "老王", "B", "男"));
        studentList.add(new StudentExcelDTO("Java", "老王", "C", "男"));
        studentList.add(new StudentExcelDTO("Java", "老王", "D", "男"));

        studentList.add(new StudentExcelDTO("C", "老赵", "A", "男"));
        studentList.add(new StudentExcelDTO("C", "老赵", "B", "男"));
        studentList.add(new StudentExcelDTO("C", "老赵", "C", "男"));
        studentList.add(new StudentExcelDTO("C", "老王", "D", "男"));
        return studentList;
    }

    private List<CourseEntity> getCourseFromDB() {
        List<StudentEntity> studentList = Lists.newArrayList();
        studentList.add(new StudentEntity("1", "A", 1, new Date(), new Date()));
        studentList.add(new StudentEntity("2", "B", 2, new Date(), new Date()));
        studentList.add(new StudentEntity("3", "C", 1, new Date(), new Date()));
        TeacherEntity teacherEntity = new TeacherEntity("老王");
        CourseEntity courseA = new CourseEntity("Java", teacherEntity, studentList);
        CourseEntity courseB = new CourseEntity("Python", teacherEntity, studentList);
        CourseEntity courseC = new CourseEntity("Scala", teacherEntity, studentList);
        CourseEntity courseD = new CourseEntity("C#", teacherEntity, Lists.newArrayList(new StudentEntity("3", "C", 1, new Date(), new Date())));
        List<CourseEntity> courseList = Lists.newArrayList();
        courseList.add(courseA);
        courseList.add(courseB);
        courseList.add(courseC);
        courseList.add(courseD);
        return courseList;
    }

    private List<CourseExcelDTO> getCourseExcelDTOFromDB(){
        List<CourseExcelDTO> list = Lists.newArrayList();
        CourseExcelDTO dtoA = CourseExcelDTO.builder().birthday(new Date())
                .courseName("Java")
                .name("A")
                .registrationDate(new Date())
                .sex(1).build();
        for (int i = 0; i < 5; i++) {
            list.add(dtoA);
        }
        return list;
    }


}
