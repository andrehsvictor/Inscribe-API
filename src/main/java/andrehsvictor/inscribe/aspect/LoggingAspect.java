package andrehsvictor.inscribe.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* andrehsvictor.inscribe.service.*.*(..))")
    public void service() {
    }

    @Before("service()")
    public void logService(JoinPoint joinPoint) {
        logger.info("Service method called: {}\nArguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @After("service()")
    public void logServiceAfter(JoinPoint joinPoint) {
        logger.info("Service method finished: {}", joinPoint.getSignature());
    }

    @AfterThrowing(pointcut = "service()", throwing = "e")
    public void logServiceException(JoinPoint joinPoint, Exception e) {
        logger.error("Service method threw an exception: {}\nArguments: {}\nException: {}",
                joinPoint.getSignature(), joinPoint.getArgs(), e.getMessage());
    }

}
