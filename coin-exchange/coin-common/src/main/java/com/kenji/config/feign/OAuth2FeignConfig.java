package com.kenji.config.feign;

import com.kenji.constant.Constants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Kenji
 * @Date 2021/8/22 15:25
 * @Description 因为有authorization-server,请求资源都要有token,因此远程调用时，需要将请求的存在请求头的token传到受访问资源
 */
@Slf4j
public class OAuth2FeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String header = null;
        if (requestAttributes == null) {
            log.info("没有请求的上下文，故无法进行token的传递");
            header = "bearer "+ Constants.INSIDE_TOEKN;
        }else {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            header = request.getHeader(HttpHeaders.AUTHORIZATION);
        }
        if (!StringUtils.isEmpty(header)) {
            requestTemplate.header(HttpHeaders.AUTHORIZATION,header);
            log.info("本次传递的token为{}",header);
        }
    }
}
