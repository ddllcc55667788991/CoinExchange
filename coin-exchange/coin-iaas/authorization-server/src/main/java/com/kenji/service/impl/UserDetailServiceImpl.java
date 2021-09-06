package com.kenji.service.impl;

import com.kenji.constant.LoginConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.kenji.constant.LoginConstant.ADMIN_CODE;

/**
 * @Author Kenji
 * @Date 2021/8/15 23:22
 * @Description
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 实现用户登录
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String loginType = requestAttributes.getRequest().getParameter("login_type");
        if (StringUtils.isEmpty(loginType)) {
            throw new AuthorizationServiceException("请添加login_type参数");
        }
        String grantTypeype = requestAttributes.getRequest().getParameter("grant_type");
        if (LoginConstant.REFRESH_TOKEN.equals(grantTypeype)){
            username = adjustUsername(username,loginType);      //为refresh_token时，需要id->username
        }
        UserDetails user = null;
        try {
            switch (loginType) {
                case LoginConstant.ADMIN_TYPE:
                    user = loadAdminByUsername(username);   //管理员登录
                    break;
                case LoginConstant.MEMBER_TYPE:
                    user = loadMemberByUsername(username);   //会员登录
                    break;
                default:
                    throw new AuthorizationServiceException("暂不支持的登录方式：" + loginType);
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new UsernameNotFoundException("用户：" + username + "不存在");

        }
        return user;
    }

    /**
     * 为refresh_token时，需要id->username
     * @param username
     * @param loginType
     * @return
     */
    private String adjustUsername(String username, String loginType) {
        if (loginType.equals(LoginConstant.ADMIN_TYPE)){
            return jdbcTemplate.queryForObject(LoginConstant.QUERY_ADMIN_USER_WITH_ID, String.class, username);
        }
        if (loginType.equals(LoginConstant.MEMBER_TYPE)){
            return jdbcTemplate.queryForObject(LoginConstant.QUERY_MEMBER_USER_WITH_ID,String.class,username);
        }
        return username;
    }

    /**
     * 管理员登录
     *
     * @param username
     * @return
     */
    private UserDetails loadAdminByUsername(String username) {
        return jdbcTemplate.queryForObject(LoginConstant.QUERY_ADMIN_SQL, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                if (resultSet.wasNull()) {
                    throw new UsernameNotFoundException("用户：" + username + "不存在");
                }
                Long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                int status = resultSet.getInt("status");
                User user = new User(
                        String.valueOf(id),     //id代替用户名，方便后续使用
                        password,
                        status == 1,
                        true,
                        true,
                        true,
                        getPrivilege(id));  //设置权限
                return user;
            }
        }, username);
    }

    /**
     * 通过用户的 id 获取用户的权限
     *
     * @param id
     * @return
     */
    private Collection<? extends GrantedAuthority> getPrivilege(long id) {
        String role = jdbcTemplate.queryForObject(LoginConstant.QUERY_ROLE_CODE_SQL, String.class, id);
        List<String> permission = null;
        if (role.equals(LoginConstant.ADMIN_CODE)) {
            permission = jdbcTemplate.queryForList(LoginConstant.QUERY_ALL_PERMISSIONS, String.class);
        } else {
            permission = jdbcTemplate.queryForList(LoginConstant.QUERY_PERMISSION_SQL, String.class, id);
        }
        if (permission == null && permission.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return permission.
                stream().
                distinct(). //去重
                map(
                perm -> new SimpleGrantedAuthority(perm)
        ).
                collect(Collectors.toSet());
    }

    /**
     * 会员登录
     *
     * @param username
     * @return
     */
    private UserDetails loadMemberByUsername(String username) {
        return jdbcTemplate.queryForObject(LoginConstant.QUERY_MEMBER_SQL, new RowMapper<UserDetails>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                if (resultSet.wasNull()) {
                    throw new UsernameNotFoundException("会员"+username+"不存在");
                }
                    long id = resultSet.getLong("id");
                    String password = resultSet.getString("password");
                    int status = resultSet.getInt("status");
                    return new User(
                            String.valueOf(id),
                            password,
                            status == 1,
                            true,
                            true,
                            true,
                            Arrays.asList(new SimpleGrantedAuthority(LoginConstant.MEMBER_CODE))
                    );
                }
        }, username, username);

    }


}
