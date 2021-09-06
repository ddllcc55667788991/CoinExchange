package com.kenji.feign;

import com.kenji.config.feign.OAuth2FeignConfig;
import com.kenji.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author Kenji
 * @Date 2021/8/22 16:00
 * @Description
 */
@FeignClient(name = "member-service",configuration = OAuth2FeignConfig.class,path = "/users")
public interface UserServiceFeign {
    /**
     * 用于admin-service远程调用member-service
     * @param ids
     * @return
     */
    @GetMapping("/basic/users")
//    List<UserDto> getBasicUsers(@RequestParam("ids") List<Long> ids);
    Map<Long,UserDto>  getBasicUsers(
            @RequestParam(value = "ids",required = false) List<Long> ids,
            @RequestParam(value = "userName",required = false) String userName,
            @RequestParam(value = "mobile",required = false) String mobile);


}

