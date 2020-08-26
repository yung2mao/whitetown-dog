package cn.whitetown.mshow.modo;

import lombok.Getter;
import lombok.Setter;

/**
 * websocket认证身份标识
 * @author taixian
 * @date 2020/08/26
 **/
@Getter
@Setter
public class IdentityInfo {
    private String randomId;
    private String userId;
    private String username;

    public IdentityInfo(String randomId, String userId, String username) {
        this.randomId = randomId;
        this.userId = userId;
        this.username = username;
    }

    public IdentityInfo() {
    }

}
