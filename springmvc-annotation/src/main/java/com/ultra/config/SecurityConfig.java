package com.ultra.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * EnableWebSecurity注解还配置了一个Spring
 * MVC参数解析解析器（argumentresolver），这样的话处理器方法就能够通过带有@AuthenticationPrincipal注解的参数
 * 获得认证用户的principal（或username）。它同时还配置了一个bean，在使用Spring表单绑定标签库来定义表单时，
 * 这个bean会自动添加一个隐藏的跨站请求伪造（cross-site request forgery，CSRF）token输入域。
 * 
 * @author admin
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private DataSource dataSource;

    /**
     * 配置user-detail服务
     * 
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // super.configure(auth);
        // roles()方法是authorities()方法的简写形式。roles()方法所给定的值都会添加一个“ROLE_”前缀，并将其作为权限授予给用户。
        /**
         * 基于内存的用户存储
         */
        // auth.inMemoryAuthentication().withUser("user").password("password").roles("USER").and().withUser("admin")
        // .password("password").roles("USER", "ADMIN");
        /**
         * 基于数据库的用户存储(指定查询用户名,密码,是否可用的sql,查询权限的sql,密码加密的方式)
         */
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username,password,true from user where username=?")
                .authoritiesByUsernameQuery("select username,ROLE_USER from user where username=?")
                .passwordEncoder(new BCryptPasswordEncoder());
        /**
         * 基于LDAP进行认证
         */
        // auth.ldapAuthentication().userSearchBase("ou=people").userSearchFilter("{uid={0}}").groupSearchBase("ou=groups")
        // .groupSearchFilter("member={0}").passwordCompare().passwordEncoder(new
        // Md5PasswordEncoder())
        // .passwordAttribute("passcode");
        /**
         * 基于自定义进行认证 假设我们需要认证的用户存储在非关系型数据库中，如Mongo或 Neo4j，在这种情况下，我们需要提供一个自定义的
         * UserDetailsService接口实现
         */
        // auth.userDetailsService(new )
    }

    /**
     * 配置如何通过拦截器保护请求(对每个请求进行细粒度安全性控制) 通过authenticated()或者permitAll()来控制需不需要权限控制
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 需要认证
         */
        // http.authorizeRequests().antMatchers("/spitters/me").authenticated().antMatchers(HttpMethod.POST,
        // "/spittles")
        // .authenticated().anyRequest().permitAll();
        /**
         * 需要某些角色
         */
        // http.authorizeRequests().antMatchers("/spitters/me").hasAnyAuthority("ROLE_SPITTER")
        // .antMatchers(HttpMethod.POST,
        // "/spittles").hasAnyAuthority("ROLE_SPITTER").anyRequest().permitAll();
        /**
         * 需要某些角色
         */
        // http.authorizeRequests().antMatchers("/spitters/me").hasRole("SPITTER")
        // .antMatchers(HttpMethod.POST,
        // "/spittles").hasRole("SPITTER").anyRequest().permitAll();
        /**
         * 强制通道的安全性(强制使用https请求).requiresChannel().antMatchers("/spitter/form").requiresSecure()
         */
        // http.authorizeRequests().antMatchers("/spitters/me").hasRole("SPITTER")
        // .antMatchers(HttpMethod.POST,
        // "/spittles").hasRole("SPITTER").anyRequest().permitAll().and()
        // .requiresChannel().antMatchers("/spitter/form").requiresSecure();
        /**
         * 不需要https传送的(通过HTTPS发送了对“/spitter/form”的请求，Spring Security将会把请求重定
         * 向到不安全的HTTP通道上)
         */
        // http.authorizeRequests().antMatchers("/spitters/me").hasRole("SPITTER")
        // .antMatchers(HttpMethod.POST,
        // "/spittles").hasRole("SPITTER").anyRequest().permitAll().and()
        // .requiresChannel().antMatchers("/spitter/form").requiresInsecure();
        /**
         * 防止跨站请求伪造(cross-site request forgery，CSRF) 从Spring Security 3.2开始，默认就会启用CSRF防护
         * 禁用CSRF防护功能http.csrf().disable()
         */
        // http.csrf().disable().authorizeRequests().antMatchers("/spitters/me").hasRole("SPITTER")
        // .antMatchers(HttpMethod.POST,
        // "/spittles").hasRole("SPITTER").anyRequest().permitAll().and()
        // .requiresChannel().antMatchers("/spitter/form").requiresSecure();
        /**
         * 默认登录页面formLogin().and()
         */
        // http.formLogin().and().authorizeRequests().antMatchers("/spitters/me").hasRole("SPITTER")
        // .antMatchers(HttpMethod.POST,
        // "/spittles").hasRole("SPITTER").anyRequest().permitAll();
        /**
         * 自定义登录页面formLogin().and()
         */
        // http.formLogin().loginPage("/login").and().authorizeRequests().antMatchers("/spitters/me").hasRole("SPITTER")
        // .antMatchers(HttpMethod.POST,
        // "/spittles").hasRole("SPITTER").anyRequest().permitAll();
        /**
         * 启用HTTP Basic认证(REST客户端向它使 用的服务进行认证的场景中)
         */
        // http.formLogin().loginPage("/login").and().httpBasic().realmName("Spittr").and().authorizeRequests()
        // .antMatchers("/spitters/me").hasRole("SPITTER").antMatchers(HttpMethod.POST,
        // "/spittles")
        // .hasRole("SPITTER").anyRequest().permitAll();
        /**
         * 启用Remember-me功能
         */
        // http.formLogin().loginPage("/login").and().rememberMe().tokenValiditySeconds(2419200).key("token").and()
        // .httpBasic().realmName("Spittr").and().authorizeRequests().antMatchers("/spitters/me")
        // .hasRole("SPITTER").antMatchers(HttpMethod.POST,
        // "/spittles").hasRole("SPITTER").anyRequest()
        // .permitAll();
        /**
         * 退出(重写默认的 LogoutFilter拦截路径.logoutUrl("/signout"))
         */
        http.formLogin().loginPage("/login").and().rememberMe().tokenValiditySeconds(2419200).key("token").and()
                .logout().logoutSuccessUrl("/").logoutUrl("/signout").and().httpBasic().realmName("Spittr").and()
                .authorizeRequests().antMatchers("/spitters/me").hasRole("SPITTER")
                .antMatchers(HttpMethod.POST, "/spittles").hasRole("SPITTER").anyRequest().permitAll();
    }

    /**
     * 配置Spring Security的Filter 链
     * 
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}
