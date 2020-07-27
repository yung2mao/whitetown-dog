package cn.whitetown.authea.manager;

import cn.whitetown.authea.annotation.WhiteAuthAnnotation;
import cn.whitetown.authea.modo.AuthHandleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * 资源-权限配置
 * @author taixian
 * @date 2020/07/24
 **/
public class WhiteSecurityConfigureManager implements SpringSecurityConfigureManager{
    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, WhiteAuthAnnotation> basicAuthMap;

    private Map<String,WhiteUriAuthManager> pathAuthMap;

    public WhiteSecurityConfigureManager(Map<String, WhiteAuthAnnotation> basicAuthMap,Map<String,WhiteUriAuthManager> pathAuthMap) {
        this.basicAuthMap = basicAuthMap;
        this.pathAuthMap = pathAuthMap;
    }

    /**
     * 初始化方法,必须先执行初始化方法才能执行配置资源权限的方法
     * @param authorizeRequests
     */
    public void init(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests) {
        AbstractWhiteUriAuthManager.initAuthorizeRequests(authorizeRequests);
    }

    @Override
    public void init() {}

    /**
     * 执行资源和权限配置方法
     */
    @PostConstruct
    @Override
    public void authHandle() {
        RequestMappingHandlerMapping mappingBean = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mappingBean.getHandlerMethods();
        for(Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            String[] paths = entry.getKey().getPatternsCondition().getPatterns().toArray(new String[0]);
            String type = null;
            String[] authors = null;
            WhiteAuthAnnotation authAnnotation = this.getAuthAnnotation(entry.getValue());
            if(authAnnotation != null) {
                type = authAnnotation.type();
                authors = authAnnotation.value();
                WhiteUriAuthManager whiteUriAuthManager = AuthHandleEnum.authManager(type);
                System.out.println(paths[0] + "," +type + "," + authors[0]);
                if (whiteUriAuthManager != null) {
                    whiteUriAuthManager.configurePathAuth(paths, authors);
                    Arrays.stream(paths).forEach(path->pathAuthMap.put(path,whiteUriAuthManager));
                }
            }
        }
        this.taskOver();
    }

    /**
     * 获取资源所需权限信息
     * @param path
     * @return
     */
    @Override
    public String[] getAuthorsByPath(String path) {
        WhiteUriAuthManager whiteUriAuthManager = pathAuthMap.get(path);
        return whiteUriAuthManager == null ? new String[0] : whiteUriAuthManager.getPathAuthors(path);
    }

    /**
     * 任务结束调用
     */
    @Override
    public void taskOver(){
        basicAuthMap.clear();
    }

    /**
     * 获取自定义权限注解
     * @param handlerMethod
     * @return
     */
    protected WhiteAuthAnnotation getAuthAnnotation(HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        WhiteAuthAnnotation authAnnotation = method.getAnnotation(WhiteAuthAnnotation.class);
        Class<?> claz = method.getDeclaringClass();
        String className = claz.getName();
        if(authAnnotation == null) {
            authAnnotation = basicAuthMap.get(className);
        }
        if(authAnnotation == null) {
            authAnnotation = claz.getAnnotation(WhiteAuthAnnotation.class);
            if(authAnnotation != null) {

            }
        }
        return authAnnotation;
    }

}
