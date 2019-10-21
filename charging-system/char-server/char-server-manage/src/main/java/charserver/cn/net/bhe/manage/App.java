package charserver.cn.net.bhe.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("charserver.cn.net.bhe.manage.mapper")
@EnableTransactionManagement
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
