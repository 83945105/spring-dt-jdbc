package com.dt.jdbc;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by 白超 on 2018/7/2.
 */
public class Test {

    public static void main(String[] args) throws IOException {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".text");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        Context context = new Context();
        context.setVariable("name", "张三");
        context.setVariable("array", new String[]{"1", "2", "3", "4", "5"});

        FileWriter writer = new FileWriter("result.html");
//        templateEngine.process("example", context, writer);
        String str = templateEngine.process("example", context);
        System.out.println(str);

    }
}
