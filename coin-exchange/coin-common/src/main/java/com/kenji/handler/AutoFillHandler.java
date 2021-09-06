package com.kenji.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author Kenji
 * @Date 2021/8/17 22:26
 * @Description
 */
@Component
public class AutoFillHandler implements MetaObjectHandler {
    /**
     * 新增自动填充
     * 1、创建人
     * 2、创建时间
     * 3、修改时间
     * 4/修改人
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
       Long userId =  getUserId();
        Date date = new Date();
        this.strictInsertFill(metaObject,"createBy",Long.class,userId);
        this.strictInsertFill(metaObject,"modifyBy",Long.class,userId);
        this.strictInsertFill(metaObject,"created",Date.class,date);
        this.strictInsertFill(metaObject,"lastUpdateTime",Date.class,date);

    }

    /**
     * 修改自动填充
     * 1、修改人
     * 2、修改时间
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId =  getUserId();
        this.strictInsertFill(metaObject,"modifyBy",Long.class,userId);
        Date date = new Date();
        this.strictInsertFill(metaObject,"lastUpdateTime",Date.class,date);
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            String  principal = authentication.getPrincipal().toString();
            if (principal.equals("anonymousUser")){
                return null;
            }
            return Long.parseLong(principal);
        }
        return null;
    }
}
