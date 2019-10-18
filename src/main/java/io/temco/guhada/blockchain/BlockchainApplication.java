package io.temco.guhada.blockchain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class BlockchainApplication {

    private static final String NAME = BlockchainApplication.class.getSimpleName();

    public static void main(String[] args) {
//        System.setProperty("jasypt.encryptor.password", "t3mco@dminUser");
//        log.info("{} starting...", NAME);
        SpringApplication.run(BlockchainApplication.class, args);
//        log.info("{} started ***", NAME);

    }

}

