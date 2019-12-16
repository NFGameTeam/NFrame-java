package com.noahframe.nftutorialserver;

import org.jsets.shiro.config.EnableJsetsShiro;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;

@EnableJsetsShiro
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, JtaAutoConfiguration.class, QuartzAutoConfiguration.class})
public class NftutorialserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(NftutorialserverApplication.class, args);
    }

}
