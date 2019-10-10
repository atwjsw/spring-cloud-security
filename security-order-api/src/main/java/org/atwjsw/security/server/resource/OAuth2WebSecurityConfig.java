package org.atwjsw.security.server.resource;

// @Configuration
// @EnableWebSecurity
// public class OAuth2WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
// @Autowired
// private UserDetailsService userDetailsService;
//
// @Bean
// public ResourceServerTokenServices tokenServices() {
// RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
// remoteTokenServices.setClientId("orderService");
// remoteTokenServices.setClientSecret("123456");
// remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:9090/oauth/check_token");
// // provide a converter to convert username obtained by remoteTokenServices into User object
// remoteTokenServices.setAccessTokenConverter(getAccessTokenConverter());
// return remoteTokenServices;
// }
//
// // Converter interface for token service implementations that store
// // authentication data inside the token
// private AccessTokenConverter getAccessTokenConverter() {
// DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
// // Default implementation of UserAuthenticationConverter. Converts to and from an Authentication using only
// // its name and authorities
// DefaultUserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();
// userTokenConverter.setUserDetailsService(userDetailsService);
//
// // Converter for the part of the data in the token representing a user.
// accessTokenConverter.setUserTokenConverter(userTokenConverter);
// return accessTokenConverter;
// }
//
// @Bean
// @Override
// protected AuthenticationManager authenticationManager() throws Exception {
// OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
// authenticationManager.setTokenServices(tokenServices());
// return authenticationManager;
// }
//
// }
