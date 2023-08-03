package hue.xgd.ttyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author:xgd
 * @Date:2023/8/1 14:45
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceAclApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAclApplication.class,args);
    }
}
