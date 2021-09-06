package com.kenji.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.TurnoverRecord;
import com.kenji.mapper.TurnoverRecordMapper;
import com.kenji.service.TurnoverRecordService;
/**
 * @Author  Kenji
 * @Date  2021/8/24 22:23
 * @Description 
 */
@Service
public class TurnoverRecordServiceImpl extends ServiceImpl<TurnoverRecordMapper, TurnoverRecord> implements TurnoverRecordService{

}
