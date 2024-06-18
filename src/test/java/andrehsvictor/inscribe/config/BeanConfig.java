package andrehsvictor.inscribe.config;

import static org.mockito.Mockito.mock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import andrehsvictor.inscribe.service.EmailService;

@Configuration
public class BeanConfig {

    @Value("${inscribe.service.email-service.mock:true}")
    private boolean emailServiceMock;

    @Bean
    EmailService emailService() {
        return emailServiceMock ? mock(EmailService.class) : new EmailService();
    }

}
