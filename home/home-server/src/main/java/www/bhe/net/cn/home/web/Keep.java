package www.bhe.net.cn.home.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Keep {

    @GetMapping("/")
    public Object Main() {
        return "Hello world";
    }

    @GetMapping("/home")
    public Object home() throws Exception {
        return "";
    }

}
