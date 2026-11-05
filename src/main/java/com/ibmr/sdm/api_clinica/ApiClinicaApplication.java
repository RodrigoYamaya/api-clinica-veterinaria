package com.ibmr.sdm.api_clinica;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiClinicaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiClinicaApplication.class, args);
    }

}