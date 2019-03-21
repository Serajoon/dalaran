package com.serajoon.dalaran.support.importexport.service;

import com.serajoon.dalaran.common.constants.MyCharset;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Objects;

public interface ExportService {
    /**
     * 下载
     * @param fileName
     * @param response
     * @param workbook
     * @param contentType
     */
    default void downLoad(String fileName, HttpServletResponse response, Workbook workbook, String contentType) {
        try {
            response.setCharacterEncoding(MyCharset.DEFAULT_CHARSET_TEXT);
            response.setHeader("content-Type", contentType);
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, MyCharset.DEFAULT_CHARSET_TEXT));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(workbook)) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取输入流和response传输文件
     *
     * @param in       输入流
     * @param response response获取输出流
     */
    default void transferFile(InputStream in, HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream()) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) > 0) {
                response.getOutputStream().write(bytes, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(in)) {
                try {
                    in.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
