package cn.whitetown.usersecurity.config;

import cn.whitetown.usersecurity.filter.TokenCheckFilter;
import cn.whitetown.usersecurity.service.DefaultUserDetailService;
import cn.whitetown.usersecurity.util.securityHandler.NotLoginHandler;
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
    private DefaultUserDetailService userDetailsService;

    @Autowired
    private NotLoginHandler notLoginHandler;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private TokenCheckFilter tokenCheckFilter;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().  //开启跨域
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  //关闭session

        /**
         * 异常处理
         */
        http.exceptionHandling().authenticationEntryPoint(notLoginHandler);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        // 防止iframe 造成跨域
        http.headers().frameOptions().disable();

        http.authorizeRequests().antMatchers("/erus/login","/erus/ver","/erus/check-capt")
                .permitAll()   //访问不受限的路径信息
                .anyRequest().authenticated()
                .and()
                .logout().permitAll();    //退出登录

        /**
         * 设置token解析过滤器
         */
        http.addFilterBefore(tokenCheckFilter, UsernamePasswordAuthenticationFilter.class);

    }

    //指定UserDetailService
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

}
