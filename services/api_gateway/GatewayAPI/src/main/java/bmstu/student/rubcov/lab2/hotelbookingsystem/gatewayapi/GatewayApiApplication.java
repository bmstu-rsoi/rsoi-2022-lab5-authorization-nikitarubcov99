package bmstu.student.rubcov.lab2.hotelbookingsystem.gatewayapi;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class GatewayApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApiApplication.class, args);
    }

    @Configuration
    static class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/api/v1/authorize").permitAll()
                    .antMatchers("/api/v1/callback").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2ResourceServer().jwt(); //or .opaqueToken();

// process CORS annotations
            http.cors();

// force a non-empty response body for 401's to make the response more browser friendly
            Okta.configureResourceServer401ResponseBody(http);
        }
    }

}
