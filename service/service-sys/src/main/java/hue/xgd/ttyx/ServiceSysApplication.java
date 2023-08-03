package hue.xgd.ttyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author:xgd
 * @Date:2023/8/2 14:51
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSysApplication.class,args);
    }
}
