#后端权限管理组件
## 基本说明
1. **功能介绍**

2.  **使用须知**

- 由于不同的开发人员可能会采用不同的授权与验证逻辑,此处针对Token校验以及Security框架验证用户所需的UserDetai仅做了抽象化的声明.
- 开发人员在使用此组件时需自行提供相应的实现,也可参考本项目auth模块中进行的实现.
- 这两个类分别为: cn.whitetown.authea.manager.TokenCheckManager 和 cn.whitetown.authea.service.WhiteUserDetailService .
