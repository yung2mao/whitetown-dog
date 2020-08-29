package cn.whitetown.mshow.controller;

import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.monitor.sys.modo.po.WhiteSysBaseInfo;
import cn.whitetown.mshow.modo.ServiceInfo;
import cn.whitetown.mshow.service.MonitorViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 监控数据展示
 * @author taixian
 * @date 2020/08/18
 **/
@RestController
@RequestMapping("/monitors")
@Validated
public class MonitorViewController {

    @Autowired
    private MonitorViewService service;

    /**
     * 获取当前服务列表
     * @return
     */
    @GetMapping("/lis")
    public ResponseData<List<ServiceInfo>> allServiceInfo() {
        return null;
    }

    @PostMapping("/base_infos")
    public ResponseData<WhiteSysBaseInfo> baseInfos(@RequestBody @NotBlank String serviceId) {
        return null;
    }
}
