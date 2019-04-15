package com.serajoon.dalaran.support.cache.rediscache.example;

import com.serajoon.dalaran.common.web.response.ResponseResult;
import com.serajoon.dalaran.support.cache.rediscache.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 缓存例子
 * redisTemplateService的操作自己去了解
 * @author  hanmeng
 * @since  2019/4/15 9:48
 */
@RestController
@RequestMapping("/api/cache/example/users")
@Api(description = "缓存例子")
@Slf4j
public class RedisExampleController {

    @Resource
    private RedisExampleService redisExampleService;

    /**
     * redisTemplateService的自己去操作了解
     */
    @Resource
    private RedisService<String,User> redisTemplateService;

    @GetMapping("/{id}")
    @ApiOperation("用户明细")
    public ResponseResult findById(@PathVariable String id) {
        User user = redisExampleService.findById(id);
        User user1 = redisTemplateService.valueGet("hiibmp:user:" + id);
        return ResponseResult.build().success(user);
    }

    @PostMapping
    @ApiOperation("新增用户")
    public ResponseResult add(@RequestBody User user) {
        User user1 = redisExampleService.saveUser(user);
        if(user1!=null){
            return ResponseResult.build().success(null,"");
        }else{
            return ResponseResult.build().failed();
        }
    }

    @PutMapping
    @ApiOperation("修改用户")
    public ResponseResult update(@RequestBody User user) {
        User user1 = redisExampleService.updateUser(user);
        if(user1!=null){
            return ResponseResult.build().success(null,"");
        }else{
            return ResponseResult.build().failed();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    public ResponseResult delete(@ApiParam(value = "用户id") @PathVariable String id) {
        int i = redisExampleService.deleteUser(id);
        return ResponseResult.build().success(null,"");
    }
}
