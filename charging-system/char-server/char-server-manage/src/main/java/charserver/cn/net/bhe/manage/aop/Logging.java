package charserver.cn.net.bhe.manage.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import charserver.cn.net.bhe.common.dict.Const;
import cn.net.bhe.utils.main.SerializeUtils;

@Component
@Aspect
public class Logging {

    static final Logger LOGGER = LoggerFactory.getLogger(Logging.class);

    @Pointcut("execution(public * charserver.cn.net.bhe.manage.base.RedisTemplateUtils.*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        String log = joinPoint.toString() + "\r\n";

        Object[] args = joinPoint.getArgs();
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        for (int i = 0; i < args.length; i++) {
            String name = codeSignature.getParameterNames()[i];
            Object value = args[i];
            if (args[i] instanceof byte[]) {
                value = SerializeUtils.deserialize((byte[]) args[i]);
            }
            log += name + ": " + value;
            if (i + 1 < args.length) {
                log += "\r\n";
            }
        }

        LOGGER.info(Const.LOGGER_FORMAT_INFO, log);
    }

    @AfterReturning(value = "pointcut()", returning = "returnVal")
    public void afterReturning(JoinPoint joinPoint, Object returnVal) throws Throwable {
        String log = joinPoint.toString() + "\r\n";
        if (returnVal instanceof byte[]) {
            log += SerializeUtils.deserialize((byte[]) returnVal);
        } else {
            log += returnVal;
        }
        LOGGER.info(Const.LOGGER_FORMAT_INFO, log);
    }

}
