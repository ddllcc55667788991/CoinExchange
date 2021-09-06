package com.kenji.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;

/**
 * @Author Kenji
 * @Date 2021/8/15 10:18
 * @Description
 */
@EnableAuthorizationServer  //开启授权服务器
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Qualifier("userDetailServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;


//    @Autowired
//    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 配置第三方客户端
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("coin-api")   //第三方客户端的名称
                .scopes("all")   //第三方客户端的授权范围
                .secret(passwordEncoder.encode("coin-secret"))   //第三方客户端的秘钥
                .authorizedGrantTypes("password", "refresh_token") //授权类型
                .accessTokenValiditySeconds(7 * 24 * 3600)   //token的有效期
                .refreshTokenValiditySeconds(30 * 24 * 3600) //refresh_token的有效期
                .and()
                .withClient("inside_app")
                .secret(passwordEncoder.encode("inside-secret"))
                .scopes("all")
                .authorizedGrantTypes("client_credentials")
                .accessTokenValiditySeconds(Integer.MAX_VALUE);
        super.configure(clients);
    }


    /**
     * 设置授权管理器和UserDetailsService
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
//                .tokenStore(redisTokenStore());
                .tokenStore(jwtTokenStore())
                .tokenEnhancer(jwtTokenEnhancer());
        super.configure(endpoints);
    }

    private TokenStore jwtTokenStore() {
        JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtTokenEnhancer());
        return jwtTokenStore;
    }

    private JwtAccessTokenConverter jwtTokenEnhancer() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //读取classpath下面的秘钥文件
        ClassPathResource classPathResource = new ClassPathResource("coinexchange.jks");
        //获取KeyStoreFactory
        KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(classPathResource, "coinexchange".toCharArray());
        //给jwtAccessTokenConverter设置秘钥对
        jwtAccessTokenConverter.setKeyPair(keyFactory.getKeyPair("coinexchange", "coinexchange".toCharArray()));
        return jwtAccessTokenConverter;
    }

//    public TokenStore redisTokenStore(){
//        return new RedisTokenStore(redisConnectionFactory);
//    }

}
