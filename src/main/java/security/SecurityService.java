package security;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author n.zhuchkevich
 * @since 09/21/2020
 * */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EntityScan(basePackages = "model.entity")
public class SecurityService {
    public static void main(String[] args) {
        run(SecurityService.class, args);
    }
}
