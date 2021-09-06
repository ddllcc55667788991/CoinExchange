package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.WorkIssue;
import com.kenji.model.R;
import com.kenji.service.WorkIssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author Kenji
 * @Date 2021/8/19 0:36
 * @Description
 */
@Api(tags = "客服工单管理")
@RequestMapping("/workIssues")
@RestController
public class WorkIssuesController {

    @Autowired
    private WorkIssueService workIssueService;

    /**
     * 根据条件分页查询
     * @param page  分页数据
     * @param status    状态
     * @param startTime 工单创建时间
     * @param endTime 工单结束时间
     * @return
     */
    @ApiOperation(value = "根据条件分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页码"),
            @ApiImplicitParam(name = "size",value = "每页大小"),
            @ApiImplicitParam(name = "status",value = "状态"),
            @ApiImplicitParam(name = "startTime",value = "工单创建时间"),
            @ApiImplicitParam(name = "endTime",value = "endTime"),
    })
    @GetMapping
    @PreAuthorize("hasAuthority('work_issue_query')")
    public R<Page<WorkIssue>> showWorkIssue(@ApiIgnore Page<WorkIssue> page,Integer status,String startTime,String endTime){
        Page<WorkIssue> workIssuePage = workIssueService.findWorkIssuesByPage(page,status,startTime,endTime);
        return R.ok(workIssuePage);
    }


    /**
     * 回复工单
     * @param workIssue
     * @return
     */
    @ApiOperation(value = "回复工单")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "workIssue",value = "回复工单的数据"),
    })
    @PatchMapping
    @PreAuthorize("hasAuthority('work_issue_update')")
    public R<Page<WorkIssue>> updateWorkIssue(WorkIssue workIssue){
        String  answerUserId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        workIssue.setAnswerUserId(Long.valueOf(answerUserId));
        boolean update = workIssueService.updateById(workIssue);
        if (update){
            return R.ok();
        }
        return R.fail("回复失败");
    }

    /**
     * 会员工单分页查询
     * @param page 分页数据
     * @return
     */
    @GetMapping("/issueList")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页"),
            @ApiImplicitParam(name = "current",value = "当前页"),
    })
    @ApiOperation(value = "会员工单分页查询")
    public R<Page<WorkIssue>> findByPage(@ApiIgnore Page<WorkIssue> page){
        Long  userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return  R.ok(workIssueService.findByPage(userId, page));
    }

    /**
     * 会员增加工单
     * @param workIssue
     * @return
     */
    @PostMapping("/addWorkIssue")
    @ApiImplicitParam(name = "workIssue",value = "会员增加的工单")
    @ApiOperation(value = "会员增加工单")
    public R addWorkIssue(@RequestBody WorkIssue workIssue){
        Long  userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        workIssue.setUserId(userId);
        workIssue.setStatus(1);
        boolean save = workIssueService.save(workIssue);
        if(save){
            return R.ok();
        }
        return R.fail("新增工单失败");
    }
}
