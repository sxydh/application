package bhe.net.cn.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import bhe.net.cn.utils.SerializeUtils;

@Component
@Aspect
public class Logging {

    static final Logger LOGGER = LoggerFactory.getLogger(Logging.class);

    @Pointcut("execution(public * bhe.net.cn.base.RedisTemplateUtils.*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        String log = joinPoint.toString() + "\n";

        Object[] args = joinPoint.getArgs();
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        for (int i = 0; i < args.length; i++) {
            String name = codeSignature.getParameterNames()[i];
            Object value = args[i];
            if (args[i] instanceof byte[]) {
                value = SerializeUtils.deserialize((byte[]) args[i]);
            }
            log += name + ": " + value + "\n";
        }

        LOGGER.info("\n\n" + log);
    }

    @AfterReturning(value = "pointcut()", returning = "returnVal")
    public void afterReturning(Object returnVal) throws Throwable {
        String log = "";
        if (returnVal instanceof byte[]) {
            log += SerializeUtils.deserialize((byte[]) returnVal);
        } else {
            log += returnVal;
        }
        System.out.println("afterReturning: \n" + log + "\n");
    }

}
