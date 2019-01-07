package com.zwd.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import com.zwd.provider.ConsumerApplication.EchoService;

/**
 * @author zwd
 * @date 2019/1/7 14:51
 * @Email stephen.zwd@gmail.com
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerApplication {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @FeignClient(name = "service-provider", fallback = EchoServiceFallback.class, configuration = FeignConfiguration.class)
    public interface EchoService {
        @RequestMapping(value = "/echo/{str}", method = RequestMethod.GET)
        String echo(@PathVariable("str") String str);

        @RequestMapping(value = "/divide", method = RequestMethod.GET)
        String divide(@RequestParam("a") Integer a, @RequestParam("b") Integer b);

        @RequestMapping(value = "/notFound", method = RequestMethod.GET)
        String notFound();
    }

}

class FeignConfiguration {
    @Bean
    public EchoServiceFallback echoServiceFallback() {
        return new EchoServiceFallback();
    }
}

class EchoServiceFallback implements EchoService {
    @Override
    public String echo(@PathVariable("str") String str) {
        System.out.println("echo==================");
        return "echo fallback";
    }

    @Override
    public String divide(@RequestParam Integer a, @RequestParam Integer b) {
        System.out.println("divide================");
        return "divide fallback";
    }

    @Override
    public String notFound() {
        System.out.println("notFound================");
        return "notFound fallback";
    }
}
