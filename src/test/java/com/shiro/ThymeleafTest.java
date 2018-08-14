package com.shiro;

import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Created by 白超 on 2018/8/14.
 */
public class ThymeleafTest {

    @Test
    void textTemplateTest() {

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setCharacterEncoding("utf-8");
        resolver.setPrefix("templates/");
        resolver.setSuffix(".text");
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setCacheable(true);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        Context context;

        for (int i = 0; i < 100; i++) {

            Long startTime = System.nanoTime();

            context = new Context();
            context.setVariable("name", "文本测试" + i);
            String text = templateEngine.process("TextTemplateTest", context);
            Long endTime = System.nanoTime() - startTime;
            System.out.println(text);

            System.out.println("耗时:" + endTime + "纳秒 " + endTime / 1000000 + "毫秒");

        }
    }

}
