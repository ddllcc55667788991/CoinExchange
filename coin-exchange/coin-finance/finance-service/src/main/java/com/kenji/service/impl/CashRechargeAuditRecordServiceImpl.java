package com.kenji.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.CashRechargeAuditRecord;
import com.kenji.mapper.CashRechargeAuditRecordMapper;
import com.kenji.service.CashRechargeAuditRecordService;
/**
 * @Author  Kenji
 * @Date  2021/8/23 0:19
 * @Description 
 */
@Service
public class CashRechargeAuditRecordServiceImpl extends ServiceImpl<CashRechargeAuditRecordMapper, CashRechargeAuditRecord> implements CashRechargeAuditRecordService{

}
