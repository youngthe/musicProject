package com.example.spring.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity

public class SpringSecurity extends WebSecurityConfigurerAdapter {

//    private final HeaderFilter headerFilter;
//
//    public SpringSecurity(HeaderFilter headerFilter) {
//        this.headerFilter = headerFilter;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.
            antMatcher("/**")
                .cors().disable()
                .csrf().disable()		//csrf방지
                .formLogin().disable();	//기본 로그인 페이지 없애기

        // 어느 위치에 추가할 것인지!
//        http.addFilterBefore(headerFilter, BasicAuthenticationFilter.class);
//         --> 디버깅 해본 결과 provider 가 두번 타더라 .. 왜그러지 ? 보니
//         해당 프로바이더를 이렇게 설정해줘서 그런거 같아서 지웠더니 한번만 타더라!
//        http.authenticationProvider(tokenAuthenticationProvider);
    }
}
