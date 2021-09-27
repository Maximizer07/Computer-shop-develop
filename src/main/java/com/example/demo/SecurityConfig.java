package com.example.demo;

import com.example.demo.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * конфигурация безопасности
 * @author mike
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends
        WebSecurityConfigurerAdapter {
    /**
     * сервис работы с пользователями
     */
    private UserService userService;
    @Autowired
    private DataSource dataSource;

    /**
     * соедение с сервисом работы с пользователями
     * @param userService  сервис работы с пользователями
     */
    @Autowired
    public void setUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param web веб конфигурация
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**");
    }

    /**
     * конфигурация аутентификации
     * @param auth аутентификация
     * @throws Exception ошибка аутентификации
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * общая конфигурация
     * @param http HttpSecurity
     * @throws Exception ошибка перехода
     */
    @Override
    protected void configure(HttpSecurity http) throws
            Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/sign-up/**","/sign-up").not().authenticated()
                //Доступ только для пользователей с ролью Администратор
                .antMatchers("/admin","/admin2").hasAuthority("ADMIN")
                .antMatchers("/user_info","/shoppingcard").hasAnyAuthority("ADMIN","USER")
                //Доступ разрешен всем пользователей
                .antMatchers("/categories/**","/product/**","/about/**").permitAll()
                //Все остальные страницы требуют аутентификации
                .anyRequest().authenticated()
                .and()
                //Настройка для входа в систему
                .formLogin()
                .loginPage("/auth")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/user_info",true)
                .permitAll()
                .and()
                .rememberMe().tokenRepository(persistentTokenRepository())
                .and()
                .logout()
                .deleteCookies("dummyCookie")
                .permitAll()
                .logoutSuccessUrl("/");
    }

    /**
     * запоминание пароля
     * @return PersistentTokenRepository
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }


}