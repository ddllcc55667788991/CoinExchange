package com.kenji.mappers;

import com.kenji.domain.User;
import com.kenji.domain.UserBank;
import com.kenji.dto.UserBankDto;
import com.kenji.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/22 15:41
 * @Description 对象的映射转化
 */
@Mapper(componentModel = "spring")
public interface UserBankDtoMappper {
    //获取该对象的实例
    UserBankDtoMappper INSTANCE = Mappers.getMapper(UserBankDtoMappper.class);

    /**
     * 将entity转化为dto
     * @param source
     * @return
     */
    UserBankDto convert2Dto(UserBank source);

    /**
     * 将dto转化entity
     * @param source
     * @return
     */
    UserBank convert2Entity(UserBankDto source);

    /**
     * 将dto转化entity
     * @param source
     * @return
     */
    List<UserBank> conver2Entity(List<UserBankDto> source);

    /**
     * 将entity转化为dto
     * @param source
     * @return
     */
    List<UserBankDto> convert2Dto(List<UserBankDto> source);
}
