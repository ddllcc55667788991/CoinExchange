package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author Kenji
 * @Date 2021/8/17 1:46
 * @Description
 */
public interface NoticeService extends IService<Notice> {


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
    Page<Notice> findNotice(Page<Notice> page, String title, String startTime, String endTime, Integer status);

    /**
     * 新增公告
     * @param notice
     * @return
     */
    int insertNotice(Notice notice);

    /**
     * 前台分页查询公告
     * @param page
     * @return
     */
    Page<Notice> fingByPage(Page<Notice> page);
}
