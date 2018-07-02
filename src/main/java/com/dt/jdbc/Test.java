package com.dt.jdbc;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by 白超 on 2018/7/2.
 */
public class Test {

    public static void main(String[] args) throws IOException {
        Test.method3();
    }

    public static void method3() throws IOException {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setCharacterEncoding("utf-8");
        resolver.setPrefix("templates/");
        resolver.setSuffix(".text");
        resolver.setCacheable(true);
        resolver.setTemplateMode(TemplateMode.TEXT);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        for (int i = 0; i < 1000; i++) {
            Context context = new Context();
            context.setVariable("name", "张三");
            context.setVariable("array", new String[]{"1", "2", "3", "4", "5"});


            Long start = System.nanoTime();

            templateEngine.process("example", context);
//            System.out.println(str);

            Long end = (System.nanoTime() - start);
            System.out.println("耗时:" + end + " => " + end / 1000000);


        }
    }

    public static void method2() {
        TemplateEngine templateEngine = new TemplateEngine();

        StringTemplateResolver resolver = new StringTemplateResolver();

        resolver.setCacheable(true);
        resolver.setTemplateMode(TemplateMode.TEXT);
        templateEngine.setTemplateResolver(resolver);
        Context context = new Context();
        context.setVariable("name", "张三");
        context.setVariable("array", new String[]{"1", "2", "3", "4", "5"});
        String template = "${name}";
        System.out.println(templateEngine.process(template, context));
    }

    public static void method1() throws IOException {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setCharacterEncoding("utf-8");
        resolver.setPrefix("templates/");
        resolver.setSuffix(".text");
        resolver.setTemplateMode(TemplateMode.TEXT);
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
