package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.NoticeMapper;
import com.kenji.domain.Notice;
import com.kenji.service.NoticeService;
import org.springframework.validation.annotation.Validated;

/**
 * @Author Kenji
 * @Date 2021/8/17 1:46
 * @Description
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

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
    @Override
    public Page<Notice> findNotice(Page<Notice> page, String title, String startTime, String endTime, Integer status) {
        return super.page(page, new LambdaQueryWrapper<Notice>()
                .like(!StringUtils.isEmpty(title), Notice::getTitle, title)
                .eq(status != null, Notice::getStatus, status)
                .gt(!StringUtils.isEmpty(startTime), Notice::getCreated, startTime + "00:00:00")
                .lt(!StringUtils.isEmpty(endTime), Notice::getCreated, endTime + "23:59:59")
        );
    }

    /**
     * 新增公告
     *
     * @param notice
     * @return
     */
    @Override
    public int insertNotice(@Validated Notice notice) {
        notice.setStatus(1);
        boolean save = super.save(notice);
        if (save) {
            return 1;
        }
        return 0;
    }

    /**
     * 前台分页查询公告
     *
     * @param page
     * @return
     */
    @Override
    public Page<Notice> fingByPage(Page<Notice> page) {
        return page(page, new LambdaQueryWrapper<Notice>()
                .eq(Notice::getStatus, 1)
                .orderByAsc(Notice::getSort));
    }
}
