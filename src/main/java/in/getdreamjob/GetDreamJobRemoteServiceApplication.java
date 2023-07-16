package in.getdreamjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GetDreamJobRemoteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetDreamJobRemoteServiceApplication.class, args);
	}

}
