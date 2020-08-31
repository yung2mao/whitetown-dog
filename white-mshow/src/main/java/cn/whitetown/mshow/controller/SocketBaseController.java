package cn.whitetown.mshow.controller;

import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.mshow.manager.SocketCache;
import cn.whitetown.mshow.modo.IdentityInfo;
import cn.whitetown.mshow.service.SocketBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * 进入监控日志系统时调用
 * @author taixian
 * @date 2020/08/26
 **/
@RestController
@RequestMapping("/ws")
public class SocketBaseController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SocketCache socketCache;

    @Autowired
    private SocketBaseService service;

    /**
     * 获取websocket连接凭证
     * @return
     */
    @GetMapping("/identity")
    public ResponseData<IdentityInfo> createIdentification() {
        Long userId = jwtTokenUtil.getUserId();
        String username = jwtTokenUtil.getUsername();
        int randLen = 12;
        IdentityInfo identityInfo = new IdentityInfo();
        String random = (WhiteToolUtil.createRandomString(randLen).hashCode() & Integer.MAX_VALUE) + "";
        identityInfo.setRandomId(random);
        identityInfo.setUserId(userId + "");
        identityInfo.setUsername(username);
        socketCache.saveConnectUser(random,userId);
        return ResponseData.ok(identityInfo);
    }

    /**
     * websocket组信息变更
     * @param groupId
     * @return
     */
    @GetMapping("/group")
    public ResponseData groupChange(@NotBlank(message = "组ID不能为空") String groupId) {
        Long userId = jwtTokenUtil.getUserId();
        service.groupChange(userId,groupId);
        return ResponseData.ok();
    }

}
