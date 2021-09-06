package com.kenji.controller;

import com.kenji.domain.CashRechargeAuditRecord;
import com.kenji.model.R;
import com.kenji.service.CashRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Kenji
 * @Date 2021/8/24 10:03
 * @Description
 */
@Api(tags = "GCN充值控制器cashRecharge")
@RequestMapping("/cashRecharge")
@RestController
public class CashRechargeController {

    @Autowired
    private CashRechargeService cashRechargeService;

    /**
     * 场外交易充值审核
     * @param cashRechargeAuditRecord 前端传来的提现审核记录
     * @return
     */
    @PostMapping("/cashRechargeUpdateStatus")
    @ApiOperation(value = "场外交易充值审核")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "cashRechargeAuditRecord",value = "前端传来的提现审核记录"),
    })
    public R cashRechargeUpdateStatus(@RequestBody CashRechargeAuditRecord cashRechargeAuditRecord){
        Long userid = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Long id = cashRechargeAuditRecord.getId();
        String remark = cashRechargeAuditRecord.getRemark();
        Byte status = cashRechargeAuditRecord.getStatus();
        Boolean isOk = cashRechargeService.updateStatus(userid,id,remark,status);
        if (isOk){
            return R.ok();
        }
        return R.fail("审核失败");
    }
}
