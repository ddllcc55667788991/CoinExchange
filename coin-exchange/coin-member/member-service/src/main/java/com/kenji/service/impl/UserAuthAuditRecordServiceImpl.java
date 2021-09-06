package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.UserAuthAuditRecordMapper;
import com.kenji.domain.UserAuthAuditRecord;
import com.kenji.service.UserAuthAuditRecordService;
/**
 * @Author  Kenji
 * @Date  2021/8/19 10:36
 * @Description 
 */
@Service
public class UserAuthAuditRecordServiceImpl extends ServiceImpl<UserAuthAuditRecordMapper, UserAuthAuditRecord> implements UserAuthAuditRecordService{


    /**
     * 根据authCode获取用户认证审核记录
     *
     * @param authCode
     * @return
     */
    @Override
    public List<UserAuthAuditRecord> getAuthAuditRecord(Long authCode) {
        return super.list(new LambdaQueryWrapper<UserAuthAuditRecord>()
                .eq(authCode!=null,UserAuthAuditRecord::getAuthCode,authCode));
    }
}
