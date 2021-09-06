package com.kenji.vo;

import cn.hutool.system.UserInfo;
import com.kenji.domain.User;
import com.kenji.domain.UserAuthAuditRecord;
import com.kenji.domain.UserAuthInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/19 21:31
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class UserAuthInfoVo {

    @ApiModelProperty(value = "用户")
    private User user;

    @ApiModelProperty(value = "用户认证审核记录")
    private List<UserAuthAuditRecord> userAuthAuditRecords;

    @ApiModelProperty(value = "用户认证信息")
    private  List<UserAuthInfo> userAuthInfos;
}
