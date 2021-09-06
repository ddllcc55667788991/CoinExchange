package com.kenji.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.CoinWithdrawAuditRecordMapper;
import com.kenji.domain.CoinWithdrawAuditRecord;
import com.kenji.service.CoinWithdrawAuditRecordService;
/**
 * @Author  Kenji
 * @Date  2021/8/23 0:19
 * @Description 
 */
@Service
public class CoinWithdrawAuditRecordServiceImpl extends ServiceImpl<CoinWithdrawAuditRecordMapper, CoinWithdrawAuditRecord> implements CoinWithdrawAuditRecordService{

}
