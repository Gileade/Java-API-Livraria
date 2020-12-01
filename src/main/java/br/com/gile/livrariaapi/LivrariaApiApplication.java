package br.com.gile.livrariaapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class LivrariaApiApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Scheduled(cron = "0 37 15 1/1 * ?")//http://www.cronmaker.com/
	public void teste(){
		System.out.println("Executou a parada");
	}

	public static void main(String[] args) {
		SpringApplication.run(LivrariaApiApplication.class, args);
	}

}
