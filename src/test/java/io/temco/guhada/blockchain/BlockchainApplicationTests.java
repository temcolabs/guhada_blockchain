package io.temco.guhada.blockchain;

import com.google.zxing.WriterException;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class BlockchainApplicationTests {

    //@Test
    public void contextLoads() throws IOException, WriterException {

        System.setProperty("jasypt.encryptor.password", "t3mco@dminUser");

        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword("t3mco@dminUser"); //2번 설정의 암호화 키를 입력

        String enc = pbeEnc.encrypt("11"); //암호화 할 내용
        System.out.println("enc = " + enc); //암호화 한 내용을 출력


        //테스트용 복호화
        String des = pbeEnc.decrypt(enc);
        System.out.println("des = " + des);




    }

}

