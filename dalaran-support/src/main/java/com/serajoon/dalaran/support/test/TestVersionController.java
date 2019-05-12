package com.serajoon.dalaran.support.test;

import com.serajoon.dalaran.common.annotations.web.version.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * {@code @ApiVersion}测试类
 * 同时测试返回{@code java.util.Dat}类型,返回的是自定义配置的格式,而不是Long类型的数字
 * <pre>
 * application.yml
 * spring:
 *     jackson:
 *         date-format: yyyy-MM-dd HH:mm:ss
 *         time-zone: GMT+8
 * </pre>
 */
@RequestMapping("/{version}")
@Api(description = "测试ApiVersion", tags = "TestVersionController")
@RestController
public class TestVersionController {


    //http://localhost:8000/demo/v1/hello
    @GetMapping("/hello")
    @ApiOperation("测试")
    @ApiVersion(1)
    public MyDate hello1() {
        return new MyDate(1, new Date());
    }

    //http://localhost:8000/demo/v2/hello
    @GetMapping("/hello")
    @ApiVersion(2)
    @ApiOperation("测试")
    public MyDate hello2() {
        return new MyDate(2, new Date());
    }

    //http://localhost:8000/demo/v3/hello
    @GetMapping("/hello")
    @ApiVersion(3)
    @ApiOperation("测试")
    public MyDate hello3() {
        return new MyDate(3, new Date());
    }
}

@Getter
@Setter
@AllArgsConstructor
class MyDate {
    private int n;
    private Date date;
}