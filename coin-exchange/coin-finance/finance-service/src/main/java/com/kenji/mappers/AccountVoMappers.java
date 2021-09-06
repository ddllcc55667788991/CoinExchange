package com.kenji.mappers;

import com.kenji.domain.Account;
import com.kenji.vo.AccountVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/25 18:41
 * @Description
 */
@Mapper(componentModel = "spring")
public interface AccountVoMappers {

    AccountVoMappers mappers = Mappers.getMapper(AccountVoMappers.class);

    /**
     * source转化为AccountVo
     * @param source
     * @return
     */
    AccountVo toConvertVo(Account source);

    /**
     * source转化为Entity
     * @param source
     * @return
     */
    Account toConvertEntity(AccountVo source);
    /**
     * source转化为List<AccountVo>
     * @param source
     * @return
     */
    List<AccountVo> toConvertVo(List<Account> source);

    /**
     * source转化为List<Account>
     * @param source
     * @return
     */
    List<Account> toConvertEntity(List<AccountVo> source);
}
