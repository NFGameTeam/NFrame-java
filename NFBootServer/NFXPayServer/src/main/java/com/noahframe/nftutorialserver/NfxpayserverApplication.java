package com.noahframe.nftutorialserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, JtaAutoConfiguration.class, QuartzAutoConfiguration.class})
public class NfxpayserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(NfxpayserverApplication.class, args);
    }

}
