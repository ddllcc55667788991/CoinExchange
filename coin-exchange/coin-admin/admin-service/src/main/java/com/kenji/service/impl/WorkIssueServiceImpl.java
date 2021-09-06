package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.dto.UserDto;
import com.kenji.feign.UserServiceFeign;
import com.kenji.model.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.WorkIssue;
import com.kenji.mapper.WorkIssueMapper;
import com.kenji.service.WorkIssueService;
import org.springframework.util.CollectionUtils;

/**
 * @Author Kenji
 * @Date 2021/8/17 1:46
 * @Description
 */
@Service
public class WorkIssueServiceImpl extends ServiceImpl<WorkIssueMapper, WorkIssue> implements WorkIssueService {

    @Autowired
    private UserServiceFeign userServiceFeign;

    /**
     * 根据条件分页查询
     *
     * @param page      分页数据
     * @param status    状态
     * @param startTime 工单创建时间
     * @param endTime   工单结束时间
     * @return
     */
    @Override
    public Page<WorkIssue> findWorkIssuesByPage(Page<WorkIssue> page, Integer status, String startTime, String endTime) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<WorkIssue> workIssuePage = super.page(page, new LambdaQueryWrapper<WorkIssue>()
                .eq(status != null, WorkIssue::getStatus, status)
                .between(!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime), WorkIssue::getCreated, startTime, endTime + " 23:59:59"));
        List<Long> userIds = workIssuePage.getRecords().stream().map(WorkIssue::getUserId).collect(Collectors.toList());
        Map<Long, UserDto> basicUsers = userServiceFeign.getBasicUsers(userIds, null, null);
        if (CollectionUtils.isEmpty(workIssuePage.getRecords())){
            return workIssuePage;
        }
        workIssuePage.getRecords().forEach(workIssue -> {
            UserDto userDto = basicUsers.get(workIssue.getUserId());
            workIssue.setUsername(userDto==null?"测试用户":userDto.getUsername());
            workIssue.setRealName(userDto==null?"测试用户":userDto.getRealName());
        });
        return workIssuePage;


    }

    /**
     * 会员工单分页查询
     *
     * @param userId 会员id
     * @param page   分页数据
     * @return
     */
    @Override
    public Page<WorkIssue> findByPage(Long userId, Page<WorkIssue> page) {
        return page(page,new LambdaQueryWrapper<WorkIssue>().eq(WorkIssue::getUserId,userId));
    }
}
