package com.ride.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ride.common.Result;
import com.ride.dto.TcmUserDTO;
import com.ride.entity.TcmUser;
import com.ride.service.TcmUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 医药论坛用户管理控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/users")
@Validated
@Tag(name = "医药论坛用户管理", description = "医药论坛用户CRUD操作相关接口")
public class TcmUserController {
    
    private static final Logger log = LoggerFactory.getLogger(TcmUserController.class);
    
    @Autowired
    private TcmUserService tcmUserService;
    
    /**
     * 创建医药论坛用户
     */
    @Operation(summary = "创建医药论坛用户", description = "根据用户信息创建新的医药论坛用户")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "用户创建成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public Result<TcmUserDTO> createUser(@RequestBody TcmUser user) {
        log.info("接收到创建医药论坛用户请求：{}", user.getUsername());
        
        TcmUserDTO result = tcmUserService.createUser(user);
        return Result.success("用户创建成功", result);
    }
    
    /**
     * 根据ID查询医药论坛用户
     */
    @Operation(summary = "根据ID查询医药论坛用户", description = "根据用户ID查询医药论坛用户详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "用户不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<TcmUserDTO> getUserById(@Parameter(description = "用户ID", required = true, example = "1") 
                                        @PathVariable Long id) {
        log.debug("接收到查询医药论坛用户请求，ID：{}", id);
        
        TcmUserDTO result = tcmUserService.getUserById(id);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据用户名查询医药论坛用户
     */
    @Operation(summary = "根据用户名查询医药论坛用户", description = "根据用户名查询医药论坛用户详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "用户不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/username/{username}")
    public Result<TcmUserDTO> getUserByUsername(@Parameter(description = "用户名", required = true, example = "zhangsan") 
                                              @PathVariable String username) {
        log.debug("接收到查询医药论坛用户请求，用户名：{}", username);
        
        TcmUserDTO result = tcmUserService.getUserByUsername(username);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据邮箱查询医药论坛用户
     */
    @Operation(summary = "根据邮箱查询医药论坛用户", description = "根据邮箱查询医药论坛用户详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "用户不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/email/{email}")
    public Result<TcmUserDTO> getUserByEmail(@Parameter(description = "用户邮箱", required = true, example = "zhangsan@example.com") 
                                           @PathVariable String email) {
        log.debug("接收到查询医药论坛用户请求，邮箱：{}", email);
        
        TcmUserDTO result = tcmUserService.getUserByEmail(email);
        return Result.success("查询成功", result);
    }
    
    /**
     * 查询所有医药论坛用户
     */
    @Operation(summary = "查询所有医药论坛用户", description = "查询系统中所有医药论坛用户信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping
    public Result<List<TcmUserDTO>> getAllUsers() {
        log.debug("接收到查询所有医药论坛用户请求");
        
        List<TcmUserDTO> result = tcmUserService.getAllUsers();
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据状态查询医药论坛用户
     */
    @Operation(summary = "根据状态查询医药论坛用户", description = "根据用户状态查询医药论坛用户列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "状态参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/status/{status}")
    public Result<List<TcmUserDTO>> getUsersByStatus(@Parameter(description = "用户状态（1:启用, 0:禁用）", required = true, example = "1") 
                                                   @PathVariable Integer status) {
        log.debug("接收到按状态查询医药论坛用户请求，状态：{}", status);
        
        List<TcmUserDTO> result = tcmUserService.getUsersByStatus(status);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据专业查询医药论坛用户
     */
    @Operation(summary = "根据专业查询医药论坛用户", description = "根据用户专业查询医药论坛用户列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/profession/{profession}")
    public Result<List<TcmUserDTO>> getUsersByProfession(@Parameter(description = "用户专业", required = true, example = "中医内科") 
                                                       @PathVariable String profession) {
        log.debug("接收到按专业查询医药论坛用户请求，专业：{}", profession);
        
        List<TcmUserDTO> result = tcmUserService.getUsersByProfession(profession);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据医院查询医药论坛用户
     */
    @Operation(summary = "根据医院查询医药论坛用户", description = "根据用户医院查询医药论坛用户列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/hospital/{hospital}")
    public Result<List<TcmUserDTO>> getUsersByHospital(@Parameter(description = "用户医院", required = true, example = "北京中医药大学附属医院") 
                                                     @PathVariable String hospital) {
        log.debug("接收到按医院查询医药论坛用户请求，医院：{}", hospital);
        
        List<TcmUserDTO> result = tcmUserService.getUsersByHospital(hospital);
        return Result.success("查询成功", result);
    }
    
    /**
     * 更新医药论坛用户
     */
    @Operation(summary = "更新医药论坛用户", description = "根据用户ID更新医药论坛用户信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "用户更新成功"),
        @ApiResponse(responseCode = "404", description = "用户不存在"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}")
    public Result<TcmUserDTO> updateUser(@Parameter(description = "用户ID", required = true, example = "1") 
                                       @PathVariable Long id, 
                                       @RequestBody TcmUser user) {
        log.info("接收到更新医药论坛用户请求，ID：{}", id);
        
        TcmUserDTO result = tcmUserService.updateUser(id, user);
        return Result.success("用户更新成功", result);
    }
    
    /**
     * 删除医药论坛用户
     */
    @Operation(summary = "删除医药论坛用户", description = "根据用户ID删除医药论坛用户")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "用户删除成功"),
        @ApiResponse(responseCode = "404", description = "用户不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> deleteUser(@Parameter(description = "用户ID", required = true, example = "1") 
                                                @PathVariable Long id) {
        log.info("接收到删除医药论坛用户请求，ID：{}", id);
        
        tcmUserService.deleteUser(id);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "用户删除成功");
        data.put("id", id);
        
        return Result.success("删除成功", data);
    }
    
    /**
     * 批量删除医药论坛用户
     */
    @Operation(summary = "批量删除医药论坛用户", description = "根据ID列表批量删除医药论坛用户")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "批量删除成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或用户不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/batch")
    public Result<Map<String, Object>> batchDeleteUsers(@Parameter(description = "用户ID列表", required = true, example = "[1, 2, 3]") 
                                                       @RequestBody List<Long> ids) {
        log.info("接收到批量删除医药论坛用户请求，数量：{}", ids.size());
        
        tcmUserService.batchDeleteUsers(ids);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "批量删除用户成功");
        data.put("count", ids.size());
        
        return Result.success("批量删除成功", data);
    }
    
    /**
     * 检查用户名是否存在
     */
    @Operation(summary = "检查用户名是否存在", description = "检查指定用户名是否已被注册")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "检查成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/check/username")
    public Result<Map<String, Object>> checkUsernameExists(@Parameter(description = "用户名", required = true, example = "zhangsan") 
                                                         @RequestParam String username) {
        log.debug("接收到检查用户名存在请求，用户名：{}", username);
        
        boolean exists = tcmUserService.existsByUsername(username);
        
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("exists", exists);
        data.put("message", exists ? "用户名已存在" : "用户名可用");
        
        return Result.success("检查成功", data);
    }
    
    /**
     * 检查邮箱是否存在
     */
    @Operation(summary = "检查邮箱是否存在", description = "检查指定邮箱是否已被注册")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "检查成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/check/email")
    public Result<Map<String, Object>> checkEmailExists(@Parameter(description = "用户邮箱", required = true, example = "zhangsan@example.com") 
                                                       @RequestParam String email) {
        log.debug("接收到检查邮箱存在请求，邮箱：{}", email);
        
        boolean exists = tcmUserService.existsByEmail(email);
        
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("exists", exists);
        data.put("message", exists ? "邮箱已存在" : "邮箱可用");
        
        return Result.success("检查成功", data);
    }
    
    /**
     * 获取发帖最多的5个用户信息
     */
    @Operation(summary = "获取发帖最多的用户", description = "获取系统中发帖数量最多的5个用户信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/stats/active")
    public Result<List<Map<String, Object>>> getActiveUserStats() {
        log.info("=============== Received request for top 5 users by post count");
        
        List<Map<String, Object>> topUsers = tcmUserService.getTop5UsersByPostCount();
        
        return Result.success("查询成功", topUsers);
    }
    
    /**
     * 按专业统计用户数量
     */
    @Operation(summary = "按专业统计用户数量", description = "根据专业统计用户数量")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/stats/by-specialty")
    public Result<Map<String, Long>> getUserCountBySpecialty(@Parameter(description = "专业名称", required = true, example = "中医内科") 
                                                           @RequestParam String profession) {
        log.debug("接收到按专业统计用户数量请求，专业：{}", profession);
        
        long count = tcmUserService.getUserCountByProfession(profession);
        
        Map<String, Long> result = new HashMap<>();
        result.put(profession, count);
        return Result.success("统计成功", result);
    }
    
    /**
     * 获取用户信息
     * 根据用户ID获取用户详细信息
     */
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "获取成功"),
        @ApiResponse(responseCode = "404", description = "用户不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/info/{userId}")
    public Result<TcmUserDTO> getUserInfo(@Parameter(description = "用户ID", required = true, example = "1") 
                                         @PathVariable Long userId) {
        log.debug("=============== Received request to get user info for userId: {}", userId);
        
        TcmUserDTO result = tcmUserService.getUserById(userId);
        return Result.success("获取成功", result);
    }
    
    /**
     * 修改个人信息
     * 根据用户ID更新用户个人信息
     */
    @Operation(summary = "修改个人信息", description = "根据用户ID更新用户个人信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "修改成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "404", description = "用户不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/info")
    public Result<TcmUserDTO> updateUserInfo(@RequestBody TcmUser user) {
        log.info("=============== Received request to update user info for userId: {}", user.getId());
        
        TcmUserDTO result = tcmUserService.updateUser(user.getId(), user);
        return Result.success("修改成功", result);
    }
}
