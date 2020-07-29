package cn.whitetown.authea.config;

import cn.whitetown.authea.manager.SpringSecurityConfigureManager;
import cn.whitetown.authea.manager.TokenCheckManager;
import cn.whitetown.authea.manager.WhiteSecurityConfigureManager;
import cn.whitetown.authea.manager.refuse.AuthenticationErrorHandler;
import cn.whitetown.authea.service.WhiteUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * security配置类
 * @author GrainRain
 * @date 2020/06/13 16:06
 **/
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WhiteUserDetailService userDetailsService;

    @Autowired
    private SpringSecurityConfigureManager securityConfigureManager;

    @Autowired
    private AuthenticationErrorHandler authenticationErrorHandler;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private TokenCheckManager tokenCheckManager;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /**
         * 异常处理
         */
        http.exceptionHandling().authenticationEntryPoint(authenticationErrorHandler)
                .accessDeniedHandler(accessDeniedHandler);

        http.headers().frameOptions().disable();

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequest = http.authorizeRequests()
                .antMatchers("/webjars/**", "/swagger/**", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html")
                .permitAll();
        this.authPathHandle(authorizeRequest);
        authorizeRequest.anyRequest().authenticated()
                .and()
                .logout().permitAll();

        /**
         * token解析过滤器
         */
        http.addFilterBefore(tokenCheckManager, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 处理后端接口权限配置
     * @param authorizeRequests
     */
    public void authPathHandle(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests) {
        ((WhiteSecurityConfigureManager)securityConfigureManager).init(authorizeRequests);
        securityConfigureManager.authHandle();
    }

    /**
     * 指定UserDetail
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

}
