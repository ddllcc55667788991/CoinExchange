package com.kenji.mappers;

import com.kenji.domain.User;
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
public interface UserDtoMappper {
    //获取该对象的实例
    UserDtoMappper INSTANCE = Mappers.getMapper(UserDtoMappper.class);

    /**
     * 将entity转化为dto
     * @param source
     * @return
     */
    UserDto convert2Dto(User source);

    /**
     * 将dto转化entity
     * @param source
     * @return
     */
    User convert2Entity(UserDto source);

    /**
     * 将dto转化entity
     * @param source
     * @return
     */
    List<User> conver2Entity(List<UserDto> source);

    /**
     * 将entity转化为dto
     * @param source
     * @return
     */
    List<UserDto> convert2Dto(List<User> source);
}
