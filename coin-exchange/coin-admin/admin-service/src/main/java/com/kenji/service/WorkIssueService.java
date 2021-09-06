package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.WorkIssue;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenji.model.R;

/**
 * @Author Kenji
 * @Date 2021/8/17 1:46
 * @Description
 */
public interface WorkIssueService extends IService<WorkIssue> {


    /**
     * 根据条件分页查询
     *
     * @param page      分页数据
     * @param status    状态
     * @param startTime 工单创建时间
     * @param endTime   工单结束时间
     * @return
     */
    Page<WorkIssue> findWorkIssuesByPage(Page<WorkIssue> page, Integer status, String startTime, String endTime);

    /**
     * 会员工单分页查询
     * @param page 分页数据
     * @param userId 会员id
     * @return
     */
    Page<WorkIssue> findByPage(Long userId, Page<WorkIssue> page);
}
