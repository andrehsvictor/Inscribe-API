package andrehsvictor.inscribe;

import org.springframework.boot.SpringApplication;

public class TestInscribeApplication {

	public static void main(String[] args) {
		SpringApplication.from(InscribeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
