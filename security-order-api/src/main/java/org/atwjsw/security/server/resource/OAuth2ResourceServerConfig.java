package org.atwjsw.security.server.resource;

// @Configuration
// @EnableResourceServer
// public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
// @Override
// public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
// resources.resourceId("order-server");
// }
//
// @Override
// public void configure(HttpSecurity http) throws Exception {
// // any post request will require the token attached has write.
// // any get request will require the token attached has read.
// http.authorizeRequests()
// .antMatchers(HttpMethod.POST).access("#oauth2.hasScope('write')")
// .antMatchers(HttpMethod.GET).access("#oauth2.hasScope('read')");
// }
// }
