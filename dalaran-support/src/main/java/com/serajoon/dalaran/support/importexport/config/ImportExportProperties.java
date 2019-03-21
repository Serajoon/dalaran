package com.serajoon.dalaran.support.importexport.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * application.yml配置
 * # 路径为resource目录下的excel目录
 * importexport:
 *   # 导入
 *   import-template-map:
 *     xxx: files/import/测试.xls
 *     exampleImport: files/import/导入模板例子.xls
 *   # 导出
 *   export-template-map:
 *     exampleExport: 导出例子.xls
 */
@ConfigurationProperties(prefix = "importexport")
@Component
@Getter
@Setter
public class ImportExportProperties {
    /**
     * <key,value>对应importexport中import-template-map
     */
    private Map<String,String> importTemplateMap;

    /**
     * <key,value>对应importexport中export-template-map
     */
    private Map<String,String> exportTemplateMap;

}
