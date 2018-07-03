package com.dt.jdbc.test;

import com.dt.core.engine.MySqlEngine;
import com.dt.jdbc.core.SpringJdbcEngine;
import com.dt.jdbc.norm.JdbcEngine;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by 白超 on 2018/7/2.
 */
public class Test {

    public static void main(String[] args) throws IOException {
        Test.method4();
    }

    public static void method4() {

        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        driverManagerDataSource.setUrl("jdbc:mysql://192.168.3.3:3306/null_logic?useSSL=false");
        driverManagerDataSource.setUsername("root");
        driverManagerDataSource.setPassword("root");

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(driverManagerDataSource);

        SpringJdbcEngine jdbcEngine = new SpringJdbcEngine();
        jdbcEngine.setJdbcTemplate(jdbcTemplate);

        for (int i = 0; i < 1; i++) {
            Long start = System.nanoTime();

//            Map<String, Object> record = jdbcEngine.queryByPrimaryKey(1, MySqlEngine.column(PubUserModel.class).column(table -> table));

//            PubUser user = jdbcEngine.queryByPrimaryKey(1, PubUser.class, MySqlEngine.column(PubUserModel.class).column(PubUserModel.Column::id));

/*            List<Map<String, Object>> records = jdbcEngine.queryForList(MySqlEngine.main(PubUserModel.class)
                    .where((condition, mainTable) -> condition
                            .and(mainTable.id().between(1, 2))
                            .or(mainTable.loginName().equalTo(3)))
                    .where((condition, mainTable) -> condition));*/

/*            List<PubUser> userList = jdbcEngine.queryForList(PubUser.class, MySqlEngine.main(PubUserModel.class)
                    .where((condition, mainTable) -> condition
                            .and(mainTable.id().between(1, 2))
                            .or(mainTable.loginName().equalTo(3)))
                    .where((condition, mainTable) -> condition));*/

//            Map<String, Object> one = jdbcEngine.queryOne(MySqlEngine.main(PubUserModel.class).limit(10, 1));

//            PubUser oneUser = jdbcEngine.queryOne(PubUser.class, MySqlEngine.main(PubUserModel.class).limit(100, 1));

/*            int count = jdbcEngine.queryCount(MySqlEngine.main(PubUserModel.class)
                    .group(PubUserModel.Group::id));*/

            Map<String, Object> result = jdbcEngine.queryPairColumnInMap(MySqlEngine.main(PubUserModel.class).column(table -> table.id().loginName()));

            Long end = (System.nanoTime() - start);
            System.out.println("耗时:" + end + " => " + end / 1000000);

        }

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
        for (int i = 0; i < 1; i++) {
            Context context = new Context();
            context.setVariable("name", "张三");
            context.setVariable("array", new String[]{"1", "2", "3", "4", "5"});


            Long start = System.nanoTime();

            String str = templateEngine.process("example", context);
            System.out.println(str);

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
