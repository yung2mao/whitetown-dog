package cn.whitetown.mshow.controller;

import cn.whitetown.dogbase.common.entity.dto.ResponseData;
import cn.whitetown.monitor.sys.modo.po.WhiteSysBaseInfo;
import cn.whitetown.mshow.modo.ServiceInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * 监控数据展示
 * @author taixian
 * @date 2020/08/18
 **/
@RestController
@RequestMapping("/monitors")
@Validated
public class MonitorViewController {

    @GetMapping("/lis")
    public ResponseData<ServiceInfo> allServiceInfo() {
        return null;
    }

    @PostMapping("/base_infos")
    public ResponseData<WhiteSysBaseInfo> baseInfos(@RequestBody @NotBlank String serviceId) {
        return null;
    }

}
