package com.example.demorestapi.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

// 리소스 서버 설정
// 리소스 서버는 앞서 설정해두었던 Oauth 서버와 연동되어 사용된다.
// 어떤 외부의 요청이 리소스에 접근할 때 인증이 필요하다면 Oauth 서버에서 제공하는 토큰을 이용해 확인
// 리소스 서버는 토큰기반으로 인증 정보가 있는지 없는지 확인하고 리소스 서버에 접근 제한
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("event");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .anonymous()
                .and()
            .authorizeRequests()
                .antMatchers("/swagger-ui.html", "/swagger-resources/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/**")
                    .permitAll() // 전부 허용 anoymoous로 해버리면 익명만 사용 가능함
                .anyRequest()
                .authenticated()
                .and()
            .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}