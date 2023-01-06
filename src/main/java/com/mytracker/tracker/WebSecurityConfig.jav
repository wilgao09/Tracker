import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Override
//    @Bean
//    public SecurityFilterChain configure(HttpSecurity http) throws Exception
//    {
//     return http.csrf().disable()
//                .anonymous().authorities("ROLE_ANONYMOUS")
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers(HttpMethod.GET, appConfigHolder.getSecurity().getLoginUrl()).permitAll().build();
//    }
//}
//

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	  System.out.println("SECURITY BEAN GOT READ");
    http
      .requiresChannel(channel -> 
          channel.anyRequest().requiresSecure())
      .authorizeRequests(authorize ->
          authorize.anyRequest().permitAll());

//	.authorizeHttpRequests((requests) -> requests
//			.requestMatchers("/", "/home").permitAll()
//			.anyRequest().authenticated()
//			);

    return http.build();


      
    }

}

///@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain configure(HttpSecurity security) throws Exception
//    {
//     return security.httpBasic().disable().formLogin().disable().authorizeRequests()
//                .permitAll().build();
//    }
//}
