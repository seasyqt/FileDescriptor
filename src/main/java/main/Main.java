package main;

import javax.servlet.MultipartConfigElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@ComponentScan
@Configuration
@EnableAutoConfiguration
public class Main {

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize(DataSize.ofMegabytes(300));
    factory.setMaxRequestSize(DataSize.ofMegabytes(1));
    return factory.createMultipartConfig();
  }

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }


}
