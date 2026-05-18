package lt.teamProject.SmartCarCosts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SmartCarCostsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartCarCostsApplication.class, args);
	}

}
