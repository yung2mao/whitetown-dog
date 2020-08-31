package cn.whitetown.mshow.service.wiml;

import cn.whitetown.dogbase.common.entity.enums.ResponseStatusEnum;
import cn.whitetown.dogbase.common.exception.WhResException;
import cn.whitetown.mshow.modo.ClientSession;
import cn.whitetown.mshow.modo.SessionMap;
import cn.whitetown.mshow.modo.SessionTypeGroup;
import cn.whitetown.mshow.service.SocketBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * socket操作服务实现
 * @author taixian
 * @date 2020/08/31
 **/
@Service
public class SocketBaseServiceImpl implements SocketBaseService {

    @Autowired
    private SessionTypeGroup sessionTypeGroup;

    private SessionMap sessionMap = SessionMap.sessionMap;

    @Override
    public void groupChange(Long userId, String groupId) {
        ClientSession clientSession = sessionMap.get(userId + "");
        if(clientSession == null) {
            throw new WhResException(ResponseStatusEnum.NO_THIS_WEBSOCKET);
        }
        sessionTypeGroup.session2Group(groupId,clientSession);
    }
}
