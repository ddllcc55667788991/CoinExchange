package com.kenji.controller;

import cn.hutool.core.date.DateUnit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.Notice;
import com.kenji.model.R;
import com.kenji.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

/**
 * @Author Kenji
 * @Date 2021/8/18 20:59
 * @Description
 */
@Api(tags = "公告管理")
@RestController
@RequestMapping("/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 根据条件分页查询
     *
     * @param page
     * @param title     标题
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param status    状态
     * @return
     */
    @GetMapping
    @ApiOperation(value = "根据条件分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "页码"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "title", value = "标题"),
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "status", value = "状态")
        }
    )
    @PreAuthorize("hasAuthority('notice_query')")
    public R<Page<Notice>> showNotice(Page<Notice> page, String title, String startTime, String endTime, Integer status) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return R.ok(noticeService.findNotice(page, title, startTime, endTime, status));
    }

    /**
     * 新增公告
     * @param notice
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增公告")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "notice",value = "前端传来的公告json数据")
    })
    @PreAuthorize("hasAuthority('notice_create')")
    public R addNotice(@RequestBody Notice notice){
        int i = noticeService.insertNotice(notice);
        if(i==1){
            return R.ok();
        }
        return R.fail("新增公告失败");
    }

    /**
     * 删除公告
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除公告")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids",value = "要删除的公告id")
    })
    @PreAuthorize("hasAuthority('notice_delete')")
    public R deleteNotice(@RequestBody String[] ids){
        return R.ok(noticeService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 禁用/启用公告
     * @param  notice
     * @return
     */
    @PostMapping("/updateStatus")
    @ApiOperation(value = "禁用公告")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id",value = "禁用的公告id"),
            @ApiImplicitParam(name = "status",value = "公告状态")
    })
    @PreAuthorize("hasAuthority('notice_update')")
    public R updateNoticeStatus(@ApiIgnore Notice notice){
        return R.ok(noticeService.updateById(notice));
    }

    /**
     * 更新公告
     * @param notice
     * @return
     */
    @PatchMapping
    @ApiOperation(value = "更新公告")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "notice",value = "修改后的公告json数据")
    })
    @PreAuthorize("hasAuthority('notice_update')")
    public R updateNotice(@RequestBody Notice notice){
        return R.ok(noticeService.updateById(notice));
    }

    /**
     * 前台分页查询公告
     * @param page
     * @return
     */
    @GetMapping("/simple")
    @ApiOperation("前台分页查询公告")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页码"),
            @ApiImplicitParam(name = "size",value = "每页大小"),
    })

    public R<Page<Notice>> fingByPage(Page<Notice> page){
        Page<Notice> noticePage = noticeService.fingByPage(page);
        return R.ok(noticePage);
    }

    /**
     * 前台公告详情
     * @param id
     * @return
     */
    @GetMapping("/simple/{id}")
    @ApiImplicitParam(name = "id",value = "公告id")
    @ApiOperation("前台公告详情")
    public R<Notice> getNotice(@PathVariable String id){
        Notice notice = noticeService.getById(id);
        return R.ok(notice);
    }
}
