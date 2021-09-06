package com.kenji.service;

import com.kenji.domain.UserAuthAuditRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/19 10:36
 * @Description
 */
public interface UserAuthAuditRecordService extends IService<UserAuthAuditRecord> {


    /**
     * 根据authCode获取用户认证审核记录
     *
     * @param authCode
     * @return
     */
    List<UserAuthAuditRecord> getAuthAuditRecord(Long authCode);
}
