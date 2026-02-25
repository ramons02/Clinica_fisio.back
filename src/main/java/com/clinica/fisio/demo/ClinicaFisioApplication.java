package com.clinica.fisio.demo;

import com.sun.jdi.connect.Connector;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.servlet.MultipartConfigFactory;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

@SpringBootApplication
public class ClinicaFisioApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicaFisioApplication.class, args);
	}

}