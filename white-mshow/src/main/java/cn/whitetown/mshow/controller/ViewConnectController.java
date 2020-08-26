package cn.whitetown.mshow.controller;

import cn.whitetown.authcommon.util.JwtTokenUtil;
import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.dogbase.common.util.WhiteFormatUtil;
import cn.whitetown.dogbase.common.util.WhiteToolUtil;
import cn.whitetown.mshow.modo.IdentityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 进入监控日志系统时调用
 * @author taixian
 * @date 2020/08/26
 **/
@RestController
@RequestMapping("/ws")
public class ViewConnectController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
        identityInfo.setRandomId(WhiteToolUtil.createRandomString(randLen));
        identityInfo.setRandomId(WhiteFormatUtil.base64Encode(userId + ""));
        identityInfo.setUsername(WhiteFormatUtil.base64Encode(username));
        return ResponseData.ok(identityInfo);
    }

}