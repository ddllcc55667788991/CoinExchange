package com.kenji.mappers;

import com.kenji.domain.Coin;
import com.kenji.dto.CoinDto;
import org.checkerframework.checker.units.qual.C;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/25 0:43
 * @Description
 */
@Mapper(componentModel = "spring")
public interface CoinDtoMapper {
   CoinDtoMapper INSTANCE =  Mappers.getMapper(CoinDtoMapper.class);

   CoinDto toConvertDto(Coin source);

   Coin toConvertEntity(CoinDto source);

   List<CoinDto> toConvertDto(List<Coin> source);

   List<Coin> toConvertEntity(List<CoinDto> source);
}
