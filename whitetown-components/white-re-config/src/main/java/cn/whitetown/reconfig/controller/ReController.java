package cn.whitetown.reconfig.controller;

import cn.whitetown.reconfig.manager.LockManager;
import cn.whitetown.reconfig.manager.RedisBaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author taixian
 * @date 2020/08/29
 **/
@RequestMapping("re")
@RestController
public class ReController {
    @Autowired
    private RedisBaseManager manager;

    @Autowired
    private LockManager lockManager;

    @GetMapping("/save")
    public String save(String key,String value) {
        System.out.println(key + "," + value);
        manager.save(key,value,1000L);
        return "success";
    }

    @GetMapping("/del")
    public String del(String key, String value) {
        System.out.println(key + "," + value);
        boolean b = manager.deleteIfEquals(key, value);
        return String.valueOf(b);
    }

    @GetMapping("/lock")
    public String lock() {
        boolean heLock = lockManager.addLock("hello", "123", 10L);
        boolean heUnLock = lockManager.unLock("hello", "1234");
        lockManager.initPermitNum("hjk",385L,null);
        return "success";
    }
}
