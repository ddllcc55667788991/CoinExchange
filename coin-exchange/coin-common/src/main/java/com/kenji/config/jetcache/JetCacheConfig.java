package com.kenji.config.jetcache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Kenji
 * @Date 2021/8/16 15:33
 * @Description
 */
@Configuration
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.kenji.service.impl")
public class JetCacheConfig {
}
