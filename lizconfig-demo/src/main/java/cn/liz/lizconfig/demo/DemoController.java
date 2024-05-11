package cn.liz.lizconfig.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Value("${liz.a}")
    private String a;

    @Value("${liz.b}")
    private String b;

    @Value("${liz.a}.${liz.b}")
    private String ab;

    @Autowired
    private LizDemoConfig demoConfig;

    @GetMapping("/demo")
    public String demo() {
        return "liz.a=" + a + "\n"
                + "liz.b=" + b + "\n"
                + "demo.a=" + demoConfig.getA() + "\n"
                + "demo.b=" + demoConfig.getB() + "\n";
    }
}
