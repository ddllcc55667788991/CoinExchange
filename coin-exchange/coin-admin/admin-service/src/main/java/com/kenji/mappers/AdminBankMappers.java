package com.kenji.mappers;

import com.kenji.domain.AdminBank;
import com.kenji.dto.AdminBankDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/24 15:14
 * @Description
 */
@Mapper(componentModel = "spring")
public interface AdminBankMappers {
    //获取该对象的实例
    AdminBankMappers INSTANCE = Mappers.getMapper(AdminBankMappers.class);

    /**
     * entity转化Dto
     * @param adminBank
     * @return
     */
    AdminBankDto convert2Dto(AdminBank adminBank);

    /**
     * Dto转化为entity
     * @param adminBankDto
     * @return
     */
    AdminBank convert2Entity(AdminBankDto adminBankDto);

    /**
     * entity转化Dto
     * @param adminBank
     * @return
     */
    List<AdminBankDto> convert2Dto(List<AdminBank> adminBank);

    /**
     * Dto转化为entity
     * @param adminBankDto
     * @return
     */
    List<AdminBank> convert2Entity(List<AdminBankDto> adminBankDto);
}
