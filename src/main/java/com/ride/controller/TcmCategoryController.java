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
import com.ride.dto.TcmCategoryDTO;
import com.ride.entity.TcmCategory;
import com.ride.service.TcmCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 医药论坛板块管理控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/categories")
@Validated
@Tag(name = "医药论坛板块管理", description = "医药论坛板块CRUD操作相关接口")
public class TcmCategoryController {
    
    private static final Logger log = LoggerFactory.getLogger(TcmCategoryController.class);
    
    @Autowired
    private TcmCategoryService tcmCategoryService;
    
    /**
     * 创建医药论坛板块
     */
    @Operation(summary = "创建医药论坛板块", description = "根据板块信息创建新的医药论坛板块")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "板块创建成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或板块名称已存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public Result<TcmCategoryDTO> createCategory(@RequestBody TcmCategory category) {
        log.info("=============== Received create TCM forum category request: {}", category.getName());
        
        TcmCategoryDTO result = tcmCategoryService.createCategory(category);
        return Result.success("Category created successfully", result);
    }
    
    /**
     * 根据ID查询医药论坛板块
     */
    @Operation(summary = "根据ID查询医药论坛板块", description = "根据板块ID查询医药论坛板块详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "板块不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<TcmCategoryDTO> getCategoryById(@Parameter(description = "板块ID", required = true, example = "1") 
                                                @PathVariable Long id) {
        log.debug("=============== Received query TCM forum category request, ID: {}", id);
        
        TcmCategoryDTO result = tcmCategoryService.getCategoryById(id);
        return Result.success("Query successful", result);
    }
    
    /**
     * 根据名称查询医药论坛板块
     */
    @Operation(summary = "根据名称查询医药论坛板块", description = "根据板块名称查询医药论坛板块详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "板块不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/name/{name}")
    public Result<TcmCategoryDTO> getCategoryByName(@Parameter(description = "板块名称", required = true, example = "中医内科") 
                                                  @PathVariable String name) {
        log.debug("=============== Received query TCM forum category by name request, name: {}", name);
        
        TcmCategoryDTO result = tcmCategoryService.getCategoryByName(name);
        return Result.success("Query successful", result);
    }
    
    /**
     * 查询所有医药论坛板块
     */
    @Operation(summary = "查询所有医药论坛板块", description = "查询系统中所有医药论坛板块信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("getAllCategories")
    public Result<List<TcmCategoryDTO>> getAllCategories() {
        log.info("=============== Received query all TCM forum categories request");
        
        List<TcmCategoryDTO> result = tcmCategoryService.getAllCategories();
        return Result.success("Query successful", result);
    }
    
    /**
     * 根据状态查询医药论坛板块
     */
    @Operation(summary = "根据状态查询医药论坛板块", description = "根据板块状态查询医药论坛板块列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "状态参数无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/status/{status}")
    public Result<List<TcmCategoryDTO>> getCategoriesByStatus(@Parameter(description = "板块状态（1:启用, 0:禁用）", required = true, example = "1") 
                                                            @PathVariable Integer status) {
        log.debug("=============== Received query TCM forum categories by status request, status: {}", status);
        
        List<TcmCategoryDTO> result = tcmCategoryService.getCategoriesByStatus(status);
        return Result.success("Query successful", result);
    }
    
    /**
     * 查询已启用的医药论坛板块
     */
    @Operation(summary = "查询已启用的医药论坛板块", description = "查询系统中所有已启用的医药论坛板块，按排序顺序返回")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/enabled")
    public Result<List<TcmCategoryDTO>> getEnabledCategories() {
        log.debug("=============== Received query enabled TCM forum categories request");
        
        List<TcmCategoryDTO> result = tcmCategoryService.getEnabledCategories(1);
        return Result.success("Query successful", result);
    }
    
    /**
     * 更新医药论坛板块
     */
    @Operation(summary = "更新医药论坛板块", description = "根据板块ID更新医药论坛板块信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "板块更新成功"),
        @ApiResponse(responseCode = "404", description = "板块不存在"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或板块名称已存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}")
    public Result<TcmCategoryDTO> updateCategory(@Parameter(description = "板块ID", required = true, example = "1") 
                                               @PathVariable Long id, 
                                               @RequestBody TcmCategory category) {
        log.info("=============== Received update TCM forum category request, ID: {}", id);
        
        TcmCategoryDTO result = tcmCategoryService.updateCategory(id, category);
        return Result.success("Category updated successfully", result);
    }
    
    /**
     * 删除医药论坛板块
     */
    @Operation(summary = "删除医药论坛板块", description = "根据板块ID删除医药论坛板块")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "板块删除成功"),
        @ApiResponse(responseCode = "404", description = "板块不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> deleteCategory(@Parameter(description = "板块ID", required = true, example = "1") 
                                                    @PathVariable Long id) {
        log.info("=============== Received delete TCM forum category request, ID: {}", id);
        
        tcmCategoryService.deleteCategory(id);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Category deleted successfully");
        data.put("id", id);
        
        return Result.success("Category deleted successfully", data);
    }
    
    /**
     * 批量删除医药论坛板块
     */
    @Operation(summary = "批量删除医药论坛板块", description = "根据ID列表批量删除医药论坛板块")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "批量删除成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或板块不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/batch")
    public Result<Map<String, Object>> batchDeleteCategories(@Parameter(description = "板块ID列表", required = true, example = "[1, 2, 3]") 
                                                           @RequestBody List<Long> ids) {
        log.info("=============== Received batch delete TCM forum categories request, count: {}", ids.size());
        
        tcmCategoryService.batchDeleteCategories(ids);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Batch deletion successful");
        data.put("count", ids.size());
        
        return Result.success("Batch deletion successful", data);
    }
    
    /**
     * 检查板块名称是否存在
     */
    @Operation(summary = "检查板块名称是否存在", description = "检查指定板块名称是否已存在")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "检查成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/check/name")
    public Result<Map<String, Object>> checkCategoryNameExists(@Parameter(description = "板块名称", required = true, example = "中医内科") 
                                                             @RequestParam String name) {
        log.debug("=============== Received check category name exists request, name: {}", name);
        
        boolean exists = tcmCategoryService.existsByName(name);
        
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("exists", exists);
        data.put("message", exists ? "Category name already exists" : "Category name available");
        
        return Result.success("Check successful", data);
    }
    
    /**
     * 获取板块帖子数量
     */
    @Operation(summary = "获取板块帖子数量", description = "获取指定板块的帖子数量统计")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}/post-count")
    public Result<Map<String, Object>> getPostCountByCategory(@Parameter(description = "板块ID", required = true, example = "1") 
                                                             @PathVariable Long id) {
        log.debug("=============== Received get category post count request, category ID: {}", id);
        
        long count = tcmCategoryService.getPostCountByCategory(id);
        
        Map<String, Object> data = new HashMap<>();
        data.put("categoryId", id);
        data.put("postCount", count);
        data.put("message", "Category post count obtained successfully");
        
        return Result.success("Statistics successful", data);
    }
    
    /**
     * 获取已启用板块统计
     */
    @Operation(summary = "获取已启用板块统计", description = "获取系统中已启用板块的数量统计")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/stats/enabled")
    public Result<Map<String, Object>> getEnabledCategoryCount() {
        log.debug("=============== Received get enabled category statistics request");
        
        long count = tcmCategoryService.getEnabledCategoryCount(1);
        
        Map<String, Object> data = new HashMap<>();
        data.put("enabledCategoryCount", count);
        data.put("message", "Enabled category statistics obtained successfully");
        
        return Result.success("Statistics successful", data);
    }
}
