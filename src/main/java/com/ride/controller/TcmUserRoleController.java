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
import com.ride.dto.TcmUserRoleDTO;
import com.ride.entity.TcmUserRole;
import com.ride.service.TcmUserRoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 医药论坛用户角色管理控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/user-roles")
@Validated
@Tag(name = "医药论坛用户角色管理", description = "医药论坛用户角色CRUD操作相关接口")
public class TcmUserRoleController {
    
    private static final Logger log = LoggerFactory.getLogger(TcmUserRoleController.class);
    
    @Autowired
    private TcmUserRoleService tcmUserRoleService;
    
    /**
     * 创建医药论坛用户角色
     */
    @Operation(summary = "创建医药论坛用户角色", description = "根据用户角色信息创建新的医药论坛用户角色")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "用户角色创建成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或用户不存在或角色已存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public Result<TcmUserRoleDTO> createUserRole(@RequestBody TcmUserRole userRole) {
        log.info("接收到创建医药论坛用户角色请求，用户ID：{}", userRole.getUserId());
        
        TcmUserRoleDTO result = tcmUserRoleService.createUserRole(userRole);
        return Result.success("用户角色创建成功", result);
    }
    
    /**
     * 根据ID查询医药论坛用户角色
     */
    @Operation(summary = "根据ID查询医药论坛用户角色", description = "根据用户角色ID查询医药论坛用户角色详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "用户角色不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<TcmUserRoleDTO> getUserRoleById(@Parameter(description = "用户角色ID", required = true, example = "1") 
                                                @PathVariable Long id) {
        log.debug("接收到查询医药论坛用户角色请求，ID：{}", id);
        
        TcmUserRoleDTO result = tcmUserRoleService.getUserRoleById(id);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据用户ID查询医药论坛用户角色
     */
    @Operation(summary = "根据用户ID查询医药论坛用户角色", description = "根据用户ID查询该用户的所有角色")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "用户ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/user/{userId}")
    public Result<List<TcmUserRoleDTO>> getUserRolesByUserId(@Parameter(description = "用户ID", required = true, example = "1") 
                                                           @PathVariable Long userId) {
        log.debug("接收到按用户查询医药论坛用户角色请求，用户ID：{}", userId);
        
        List<TcmUserRoleDTO> result = tcmUserRoleService.getUserRolesByUserId(userId);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据角色类型查询医药论坛用户角色
     */
    @Operation(summary = "根据角色类型查询医药论坛用户角色", description = "根据角色类型查询所有拥有该角色的用户")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "角色类型参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/role/{roleType}")
    public Result<List<TcmUserRoleDTO>> getUserRolesByRoleType(@Parameter(description = "角色类型", required = true, example = "1") 
                                                              @PathVariable String roleType) {
        log.debug("接收到按角色类型查询医药论坛用户角色请求，角色类型：{}", roleType);
        
        List<TcmUserRoleDTO> result = tcmUserRoleService.getUserRolesByRoleType(roleType);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据板块ID查询医药论坛用户角色
     */
    @Operation(summary = "根据板块ID查询医药论坛用户角色", description = "根据板块ID查询该板块下的所有版主")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "板块ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/category/{categoryId}")
    public Result<List<TcmUserRoleDTO>> getUserRolesByCategoryId(@Parameter(description = "板块ID", required = true, example = "1") 
                                                               @PathVariable Long categoryId) {
        log.debug("接收到按板块ID查询医药论坛用户角色请求，板块ID：{}", categoryId);
        
        List<TcmUserRoleDTO> result = tcmUserRoleService.getUserRolesByUserId(categoryId);
        return Result.success("查询成功", result);
    }
    
    /**
     * 查询所有医药论坛用户角色
     */
    @Operation(summary = "查询所有医药论坛用户角色", description = "查询系统中所有医药论坛用户角色信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping
    public Result<List<TcmUserRoleDTO>> getAllUserRoles() {
        log.debug("接收到查询所有医药论坛用户角色请求");
        
        List<TcmUserRoleDTO> result = tcmUserRoleService.getAllUserRoles();
        return Result.success("查询成功", result);
    }
    
    /**
     * 更新医药论坛用户角色
     */
    @Operation(summary = "更新医药论坛用户角色", description = "根据用户角色ID更新医药论坛用户角色信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "用户角色更新成功"),
        @ApiResponse(responseCode = "404", description = "用户角色不存在"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}")
    public Result<TcmUserRoleDTO> updateUserRole(@Parameter(description = "用户角色ID", required = true, example = "1") 
                                               @PathVariable Long id, 
                                               @RequestBody TcmUserRole userRole) {
        log.info("接收到更新医药论坛用户角色请求，ID：{}", id);
        
        TcmUserRoleDTO result = tcmUserRoleService.updateUserRole(id, userRole);
        return Result.success("用户角色更新成功", result);
    }
    
    /**
     * 删除医药论坛用户角色
     */
    @Operation(summary = "删除医药论坛用户角色", description = "根据用户角色ID删除医药论坛用户角色")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "用户角色删除成功"),
        @ApiResponse(responseCode = "404", description = "用户角色不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> deleteUserRole(@Parameter(description = "用户角色ID", required = true, example = "1") 
                                                    @PathVariable Long id) {
        log.info("接收到删除医药论坛用户角色请求，ID：{}", id);
        
        tcmUserRoleService.deleteUserRole(id);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "用户角色删除成功");
        data.put("id", id);
        
        return Result.success("删除成功", data);
    }
    
    /**
     * 批量删除医药论坛用户角色
     */
    @Operation(summary = "批量删除医药论坛用户角色", description = "根据ID列表批量删除医药论坛用户角色")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "批量删除成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或用户角色不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/batch")
    public Result<Map<String, Object>> batchDeleteUserRoles(@Parameter(description = "用户角色ID列表", required = true, example = "[1, 2, 3]") 
                                                           @RequestBody List<Long> ids) {
        log.info("接收到批量删除医药论坛用户角色请求，数量：{}", ids.size());
        
        tcmUserRoleService.batchDeleteUserRoles(ids);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "批量删除用户角色成功");
        data.put("count", ids.size());
        
        return Result.success("批量删除成功", data);
    }
    
    /**
     * 根据用户ID和角色类型删除用户角色
     */
    @Operation(summary = "根据用户ID和角色类型删除用户角色", description = "删除指定用户在指定板块下的角色")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/user/{userId}/category/{categoryId}/role/{roleType}")
    public Result<Map<String, Object>> deleteUserRoleByUserIdAndRoleType(@Parameter(description = "用户ID", required = true, example = "1") 
                                                                        @PathVariable Long userId,
                                                                        @Parameter(description = "板块ID", required = true, example = "1") 
                                                                        @PathVariable Long categoryId,
                                                                        @Parameter(description = "角色类型", required = true, example = "MODERATOR") 
                                                                        @PathVariable String roleType) {
        log.info("接收到根据用户和角色类型删除请求，用户ID：{}, 板块ID：{}, 角色类型：{}", userId, categoryId, roleType);
        
        tcmUserRoleService.deleteUserRoleByUserIdAndRoleType(userId, roleType);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "用户角色删除成功");
        data.put("userId", userId);
        data.put("categoryId", categoryId);
        data.put("roleType", roleType);
        
        return Result.success("删除成功", data);
    }
    
    /**
     * 删除用户的所有角色
     */
    @Operation(summary = "删除用户的所有角色", description = "删除指定用户的所有角色")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "400", description = "用户ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/user/{userId}")
    public Result<Map<String, Object>> deleteUserRolesByUserId(@Parameter(description = "用户ID", required = true, example = "1") 
                                                             @PathVariable Long userId) {
        log.info("接收到删除用户所有角色请求，用户ID：{}", userId);
        
        tcmUserRoleService.deleteUserRoleByUserId(userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "用户所有角色删除成功");
        data.put("userId", userId);
        
        return Result.success("删除成功", data);
    }
    
    /**
     * 检查用户角色是否存在
     */
    @Operation(summary = "检查用户角色是否存在", description = "检查指定用户在指定板块下的指定角色是否存在")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "检查成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/check/exists")
    public Result<Map<String, Object>> checkUserRoleExists(@Parameter(description = "用户ID", required = true, example = "1") 
                                                         @RequestParam Long userId,
                                                         @Parameter(description = "角色类型", required = true, example = "MODERATOR") 
                                                         @RequestParam String roleType,
                                                         @Parameter(description = "板块ID", required = true, example = "1") 
                                                         @RequestParam Long categoryId) {
        log.debug("接收到检查用户角色存在请求，用户ID：{}, 角色类型：{}, 板块ID：{}", userId, roleType, categoryId);
        
        boolean exists = tcmUserRoleService.existsByUserIdAndRoleType(userId, roleType, categoryId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("roleType", roleType);
        data.put("categoryId", categoryId);
        data.put("exists", exists);
        data.put("message", exists ? "用户角色已存在" : "用户角色不存在");
        
        return Result.success("检查成功", data);
    }
    
    /**
     * 查询所有系统管理员
     */
    @Operation(summary = "查询所有系统管理员", description = "查询系统中所有拥有ADMIN角色的用户")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/admins")
    public Result<List<TcmUserRoleDTO>> getAllAdmins() {
        log.debug("接收到查询所有系统管理员请求");
        
        List<TcmUserRoleDTO> result = tcmUserRoleService.getAdmins();
        return Result.success("查询成功", result);
    }
    
    /**
     * 查询所有版主
     */
    @Operation(summary = "查询所有版主", description = "查询系统中所有拥有版主角色的用户")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/moderators")
    public Result<List<TcmUserRoleDTO>> getAllModerators() {
        log.debug("接收到查询所有版主请求");
        
        List<TcmUserRoleDTO> result = tcmUserRoleService.getUserRolesByRoleType("MODERATOR");
        return Result.success("查询成功", result);
    }
    
    /**
     * 获取用户角色统计
     */
    @Operation(summary = "获取用户角色统计", description = "获取系统中所有用户角色的数量统计")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/stats/total")
    public Result<Map<String, Object>> getTotalUserRoleCount() {
        log.debug("接收到获取用户角色统计请求");
        
        long count = tcmUserRoleService.getAllUserRoles().size();
        
        Map<String, Object> data = new HashMap<>();
        data.put("totalUserRoleCount", count);
        data.put("message", "用户角色统计获取成功");
        
        return Result.success("统计成功", data);
    }
}
