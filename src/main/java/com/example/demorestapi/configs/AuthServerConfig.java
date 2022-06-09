package com.example.demorestapi.configs;

import com.example.demorestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

// Oauth2 인증 서버 설정
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	PasswordEncoder passwordEncoder;

	// 빈설정에서 다른 곳에서 사용할 수 있도록 빈으로 등록해놨음
	// AuthenticationManager는 유저인증 정보를 가지고 있음
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	AccountService accountService;

	// 이전 시큐리티 config에 빈 등록 해줬음 가져온것
	@Autowired
	TokenStore tokenStore;

	@Autowired
	AppProperties appProperties;

	// 시큐리티에서는 패스워드 인코더 설정
	// 클라이언트의 시크릿를 검증할때 패스워드 인코더 사용
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.passwordEncoder(passwordEncoder);
	}

	// 클라이언트 설정
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// 인증 서버가 지원할 grant 타입 설정
		// refresh token은 oauth 토큰을 발급받을 때 refresh 토큰도 같이 발급해주는데
		// 이걸 가지고 새로운 access 토큰을 발급받는 타입이다.
		clients.inMemory()
				.withClient(appProperties.getClientId())
				.authorizedGrantTypes("password", "refresh_token")
				.scopes("read", "write")
				.secret(this.passwordEncoder.encode(appProperties.getClientSecret()))
				.accessTokenValiditySeconds(10 * 60)
				.refreshTokenValiditySeconds(6 * 10 * 60);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager)
				.userDetailsService(accountService)
				.tokenStore(tokenStore);
	}
}