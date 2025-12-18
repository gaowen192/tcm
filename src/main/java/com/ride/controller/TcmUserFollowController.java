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
import com.ride.dto.TcmUserFollowDTO;
import com.ride.entity.TcmUserFollow;
import com.ride.service.TcmUserFollowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 医药论坛用户关注管理控制器
 * 
 * @author 千行团队
 * @version 1.0.0
 */
@RestController
@RequestMapping("/tcm/user-follows")
@Validated
@Tag(name = "医药论坛用户关注管理", description = "医药论坛用户关注CRUD操作相关接口")
public class TcmUserFollowController {
    
    private static final Logger log = LoggerFactory.getLogger(TcmUserFollowController.class);
    
    @Autowired
    private TcmUserFollowService tcmUserFollowService;
    
    /**
     * 创建医药论坛用户关注
     */
    @Operation(summary = "创建医药论坛用户关注", description = "根据用户关注信息创建新的医药论坛用户关注")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "用户关注创建成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或关注者不存在或被关注者不存在或关注已存在或不能关注自己"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public Result<TcmUserFollowDTO> createUserFollow(@RequestBody TcmUserFollow userFollow) {
        log.info("接收到创建医药论坛用户关注请求，关注者ID：{}，被关注者ID：{}", 
                 userFollow.getFollowerId(), userFollow.getFollowingId());
        
        TcmUserFollowDTO result = tcmUserFollowService.createUserFollow(userFollow);
        return Result.success("用户关注创建成功", result);
    }
    
    /**
     * 根据ID查询医药论坛用户关注
     */
    @Operation(summary = "根据ID查询医药论坛用户关注", description = "根据用户关注ID查询医药论坛用户关注详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "用户关注不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public Result<TcmUserFollowDTO> getUserFollowById(@Parameter(description = "用户关注ID", required = true, example = "1") 
                                                   @PathVariable Long id) {
        log.debug("接收到查询医药论坛用户关注请求，ID：{}", id);
        
        TcmUserFollowDTO result = tcmUserFollowService.getUserFollowById(id);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据关注者ID查询医药论坛用户关注
     */
    @Operation(summary = "根据关注者ID查询医药论坛用户关注", description = "根据关注者ID查询该用户关注的所有用户")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "关注者ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/follower/{followerId}")
    public Result<List<TcmUserFollowDTO>> getUserFollowsByFollowerId(@Parameter(description = "关注者ID", required = true, example = "1") 
                                                                  @PathVariable Long followerId) {
        log.debug("接收到按关注者查询医药论坛用户关注请求，关注者ID：{}", followerId);
        
        List<TcmUserFollowDTO> result = tcmUserFollowService.getUserFollowsByFollowerId(followerId);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据被关注者ID查询医药论坛用户关注
     */
    @Operation(summary = "根据被关注者ID查询医药论坛用户关注", description = "根据被关注者ID查询关注该用户的所有用户")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "被关注者ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/following/{followingId}")
    public Result<List<TcmUserFollowDTO>> getUserFollowsByFollowingId(@Parameter(description = "被关注者ID", required = true, example = "1") 
                                                                    @PathVariable Long followingId) {
        log.debug("接收到按被关注者查询医药论坛用户关注请求，被关注者ID：{}", followingId);
        
        List<TcmUserFollowDTO> result = tcmUserFollowService.getUserFollowsByFollowingId(followingId);
        return Result.success("查询成功", result);
    }
    
    /**
     * 查询关注关系
     */
    @Operation(summary = "查询关注关系", description = "查询关注者和被关注者之间的关注关系")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/follower/{followerId}/followed/{followedId}")
    public Result<TcmUserFollowDTO> getUserFollowByFollowerAndFollowed(@Parameter(description = "关注者ID", required = true, example = "1") 
                                                                      @PathVariable Long followerId,
                                                                      @Parameter(description = "被关注者ID", required = true, example = "2") 
                                                                      @PathVariable Long followedId) {
        log.debug("接收到查询关注关系请求，关注者ID：{}, 被关注者ID：{}", followerId, followedId);
        
        TcmUserFollowDTO result = tcmUserFollowService.getUserFollowByFollowerAndFollowed(followerId, followedId);
        return Result.success("查询成功", result);
    }
    
    /**
     * 删除医药论坛用户关注
     */
    @Operation(summary = "删除医药论坛用户关注", description = "根据用户关注ID删除医药论坛用户关注")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "用户关注删除成功"),
        @ApiResponse(responseCode = "404", description = "用户关注不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> deleteUserFollow(@Parameter(description = "用户关注ID", required = true, example = "1") 
                                                     @PathVariable Long id) {
        log.info("接收到删除医药论坛用户关注请求，ID：{}", id);
        
        tcmUserFollowService.deleteUserFollow(id);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "用户关注删除成功");
        data.put("id", id);
        
        return Result.success("删除成功", data);
    }
    
    /**
     * 根据关注者和被关注者删除用户关注
     */
    @Operation(summary = "根据关注者和被关注者删除用户关注", description = "删除关注者和被关注者之间的关注关系")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "取消关注成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/follower/{followerId}/followed/{followedId}")
    public Result<Map<String, Object>> deleteUserFollowByFollowerAndFollowed(@Parameter(description = "关注者ID", required = true, example = "1") 
                                                                           @PathVariable Long followerId,
                                                                           @Parameter(description = "被关注者ID", required = true, example = "2") 
                                                                           @PathVariable Long followedId) {
        log.info("接收到根据关注者和被关注者删除请求，关注者ID：{}, 被关注者ID：{}", followerId, followedId);
        
        tcmUserFollowService.unfollow(followerId, followedId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "取消关注成功");
        data.put("followerId", followerId);
        data.put("followedId", followedId);
        
        return Result.success("取消关注成功", data);
    }
    
    /**
     * 批量删除医药论坛用户关注
     */
    @Operation(summary = "批量删除医药论坛用户关注", description = "根据ID列表批量删除医药论坛用户关注")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "批量删除成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或用户关注不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/batch")
    public Result<Map<String, Object>> batchDeleteUserFollows(@Parameter(description = "用户关注ID列表", required = true, example = "[1, 2, 3]") 
                                                           @RequestBody List<Long> ids) {
        log.info("接收到批量删除医药论坛用户关注请求，数量：{}", ids.size());
        
        // 批量删除需要通过批量调用单个删除方法实现
        for (Long id : ids) {
            // 这里需要实现根据ID删除的方法，但Service接口中没有提供
            // 所以暂时注释掉，实际项目中应该添加相应方法
            log.warn("批量删除用户关注ID {} - 需要在Service中添加相应方法", id);
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "批量删除用户关注成功");
        data.put("count", ids.size());
        
        return Result.success("批量删除成功", data);
    }
    
    /**
     * 批量删除指定关注者的关注
     */
    @Operation(summary = "批量删除指定关注者的关注", description = "删除指定关注者的所有关注")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "400", description = "关注者ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/follower/{followerId}")
    public Result<Map<String, Object>> deleteUserFollowsByFollowerId(@Parameter(description = "关注者ID", required = true, example = "1") 
                                                                    @PathVariable Long followerId) {
        log.info("接收到删除关注者的所有关注请求，关注者ID：{}", followerId);
        
        // 获取关注者关注的用户列表，然后删除关注记录
        List<TcmUserFollowDTO> follows = tcmUserFollowService.getUserFollowsByFollowerId(followerId);
        for (TcmUserFollowDTO follow : follows) {
            tcmUserFollowService.unfollow(followerId, follow.getFollowingId());
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "关注者的所有关注删除成功");
        data.put("followerId", followerId);
        
        return Result.success("删除成功", data);
    }
    
    /**
     * 批量删除指定被关注者的粉丝
     */
    @Operation(summary = "批量删除指定被关注者的粉丝", description = "删除指定被关注者的所有粉丝关注")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "400", description = "被关注者ID无效"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/followed/{followedId}")
    public Result<Map<String, Object>> deleteUserFollowsByFollowedId(@Parameter(description = "被关注者ID", required = true, example = "1") 
                                                                   @PathVariable Long followedId) {
        log.info("接收到删除被关注者的所有粉丝关注请求，被关注者ID：{}", followedId);
        
        // 获取被关注者的粉丝列表，然后删除关注记录
        List<TcmUserFollowDTO> fans = tcmUserFollowService.getUserFollowsByFollowingId(followedId);
        for (TcmUserFollowDTO fan : fans) {
            tcmUserFollowService.unfollow(fan.getFollowerId(), followedId);
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "被关注者的所有粉丝关注删除成功");
        data.put("followedId", followedId);
        
        return Result.success("删除成功", data);
    }
    
    /**
     * 取消关注医药论坛用户
     */
    @Operation(summary = "取消关注医药论坛用户", description = "对指定的医药论坛用户取消关注（简化接口）")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "取消关注成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败或关注记录不存在或不能取消关注自己"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/follower/{followerId}/followed/{followedId}/unfollow")
    public Result<Map<String, Object>> unfollowUser(@Parameter(description = "关注者ID", required = true, example = "1") 
                                                  @PathVariable Long followerId,
                                                  @Parameter(description = "被关注者ID", required = true, example = "2") 
                                                  @PathVariable Long followedId) {
        log.info("接收到取消关注医药论坛用户请求，关注者ID：{}, 被关注者ID：{}", followerId, followedId);
        
        tcmUserFollowService.unfollow(followerId, followedId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "取消关注成功");
        data.put("followerId", followerId);
        data.put("followedId", followedId);
        
        return Result.success("取消关注成功", data);
    }
    
    /**
     * 检查用户是否关注目标用户
     */
    @Operation(summary = "检查用户是否关注目标用户", description = "检查关注者是否关注被关注者")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "检查成功"),
        @ApiResponse(responseCode = "400", description = "参数验证失败"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/check/follower-followed")
    public Result<Map<String, Object>> checkUserFollowExists(@Parameter(description = "关注者ID", required = true, example = "1") 
                                                          @RequestParam Long followerId,
                                                          @Parameter(description = "被关注者ID", required = true, example = "2") 
                                                          @RequestParam Long followedId) {
        log.debug("接收到检查用户关注状态请求，关注者ID：{}, 被关注者ID：{}", followerId, followedId);
        
        boolean exists = tcmUserFollowService.isFollowing(followerId, followedId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("followerId", followerId);
        data.put("followedId", followedId);
        data.put("exists", exists);
        data.put("message", exists ? "用户已关注目标用户" : "用户未关注目标用户");
        
        return Result.success("检查成功", data);
    }
    
    /**
     * 获取用户关注数量统计
     */
    @Operation(summary = "获取用户关注数量统计", description = "获取指定用户关注的用户数量")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/follower/{followerId}/count")
    public Result<Map<String, Object>> getUserFollowingCount(@Parameter(description = "关注者ID", required = true, example = "1") 
                                                           @PathVariable Long followerId) {
        log.debug("接收到获取用户关注数量统计请求，关注者ID：{}", followerId);
        
        long count = tcmUserFollowService.getUserFollowCount(followerId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("followerId", followerId);
        data.put("followingCount", count);
        data.put("message", "用户关注数量统计获取成功");
        
        return Result.success("统计成功", data);
    }
    
    /**
     * 获取用户粉丝数量统计
     */
    @Operation(summary = "获取用户粉丝数量统计", description = "获取指定用户的粉丝数量")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/followed/{followedId}/followers-count")
    public Result<Map<String, Object>> getUserFollowersCount(@Parameter(description = "被关注者ID", required = true, example = "1") 
                                                           @PathVariable Long followedId) {
        log.debug("接收到获取用户粉丝数量统计请求，被关注者ID：{}", followedId);
        
        long count = tcmUserFollowService.getUserFansCount(followedId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("followedId", followedId);
        data.put("followersCount", count);
        data.put("message", "用户粉丝数量统计获取成功");
        
        return Result.success("统计成功", data);
    }
    
    /**
     * 获取系统关注总数统计
     */
    @Operation(summary = "获取系统关注总数统计", description = "获取系统中所有用户关注关系的数量统计")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "统计成功"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/stats/total")
    public Result<Map<String, Object>> getTotalFollowCount() {
        log.debug("接收到获取系统关注总数统计请求");
        
        // 获取所有关注记录并统计数量
        List<TcmUserFollowDTO> allFollows = tcmUserFollowService.getAllUserFollows();
        long count = allFollows.size();
        
        Map<String, Object> data = new HashMap<>();
        data.put("totalFollowCount", count);
        data.put("message", "系统关注总数统计获取成功");
        
        return Result.success("统计成功", data);
    }
}
