package com.serajoon.dalaran.support.ftp.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * # 上传下载 业务类型编码和业务类型名称
 * updownload:
 *   business:
 *     business-map:
 *       1 : xxx
 */
@ConfigurationProperties(prefix = "updownload.business")
@Component
@Getter
@Setter
public class UpDownLoadProperties {
    /**
     * <key,value>对应sys_file_business表中的business_type和business_name
     */
    private Map<String,String> businessMap;
}
