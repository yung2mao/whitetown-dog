package cn.whitetown.authea.config;

import cn.whitetown.authea.manager.TokenCheckManager;
import cn.whitetown.authea.manager.refuse.AuthenticationErrorHandler;
import cn.whitetown.authea.service.WhiteUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author GrainRain
 * @date 2020/06/13 16:06
 **/
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WhiteUserDetailService userDetailsService;

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

        //开启跨域
        http.csrf().disable().
                //关闭session
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /**
         * 异常处理
         */
        http.exceptionHandling().authenticationEntryPoint(authenticationErrorHandler);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        // 防止iframe 造成跨域
        http.headers().frameOptions().disable();

        http.authorizeRequests().antMatchers("/erus/login","/erus/ver","/erus/check-capt",
                "/webjars/**","/swagger/**","/v2/api-docs","/swagger-resources/**","/swagger-ui.html")
                .permitAll()   //访问不受限的路径信息
                .anyRequest().authenticated()
                .and()
                .logout().permitAll();    //退出登录

        /**
         * 设置token解析过滤器
         */
        http.addFilterBefore(tokenCheckManager, UsernamePasswordAuthenticationFilter.class);
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
