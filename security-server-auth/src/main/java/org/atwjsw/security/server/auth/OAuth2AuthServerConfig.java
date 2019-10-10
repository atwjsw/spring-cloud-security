package org.atwjsw.security.server.auth;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	// Typical authentication logics (username/password, oauth2) is provided
	// e.g. if it is password flow, authentication manager will decode header
	// "basic " and get user details based on username, then encode the password
	// and then compare the password to the password returned from UserDetails
	// if matched, the Authentication object will be populated with flag and put into
	// SecurityContext
	@Autowired
	private AuthenticationManager authenticationMananger;

	@Autowired
	private DataSource dataSource;

	// TokenStore is responsible for save and fetch token and its associated
	// user and client info. by default its implementation is in memory
	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	};

	// encrypt password in Spring Security. encapsulate a encryption tool.
	// The encryption tool use a salt generated randomly.
	@Autowired
	private PasswordEncoder passwordEncoder;

	// 配置哪些用户(Resource Owner)可以来访问认证服务器
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationMananger)
				.tokenStore(tokenStore());
	}

	// configure client details. what clients will be allowed to get tokens?
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		// configure orderApp as a client which can request token from Authorization Server
		// In this case, orderApp is a webapp in browser or mobile app
		// clients.inMemory()
		// .withClient("orderApp") // client id
		// .secret(passwordEncoder.encode("123456")) // client secret
		// .scopes("read", "write") // authorization scopes available to client orderApp
		// .accessTokenValiditySeconds(3600) // 有效期
		// .resourceIds("order-server") // using the issued token, what resource server can be accessed
		// .authorizedGrantTypes("password") // what grant types can be used by client
		// // 由于订单服务要访问AuthorizationServer验证token，也需要注册为Client?
		// .and()
		// .withClient("orderService")
		// .secret(passwordEncoder.encode("123456"));
		// .scopes("read")
		// .accessTokenValiditySeconds(3600)
		// .resourceIds("order-server");
		// .authorizedGrantTypes("password;");

		// use jdbc to get client details from injected DataSource
		clients.jdbc(dataSource);

	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// the request to /oauth/token endpoint should be authenticated
		// i.e. has above client id and secret in header "Authorization Basic..."
		security.checkTokenAccess("isAuthenticated()");

	}

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("123456"));
	}
}
