package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.dto.AdminBankDto;
import com.kenji.mappers.AdminBankMappers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.AdminBankMapper;
import com.kenji.domain.AdminBank;
import com.kenji.service.AdminBankService;
import org.springframework.util.CollectionUtils;

/**
 * @Author Kenji
 * @Date 2021/8/19 1:38
 * @Description
 */
@Service
public class AdminBankServiceImpl extends ServiceImpl<AdminBankMapper, AdminBank> implements AdminBankService {

    /**
     * 根据条件分页查询
     *
     * @param page     分页数据
     * @param bankCard 银行卡号
     * @return
     */
    @Override
    public Page<AdminBank> findAdminBankByPage(Page<AdminBank> page, String bankCard) {
        Page<AdminBank> adminBankPage = page(page, new LambdaQueryWrapper<AdminBank>()
                .like(!StringUtils.isEmpty(bankCard), AdminBank::getBankCard, bankCard)
        );
        System.out.println(adminBankPage.getSize());
        return adminBankPage;
    }

    /**
     * 获取所有银行卡数据
     *
     * @return
     */
    @Override
    public List<AdminBankDto> findAdminBank() {
        List<AdminBank> list = super.list(new LambdaQueryWrapper<AdminBank>().eq(AdminBank::getStatus, 1));
        if (CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        List<AdminBankDto> adminBankDtoList = AdminBankMappers.INSTANCE.convert2Dto(list);
        return adminBankDtoList;
    }
}
