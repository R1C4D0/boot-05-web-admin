package com.atguigu.admin;

import com.atguigu.admin.view.HelloVIew;
import com.atguigu.admin.view.MainView;
import com.atguigu.admin.view.MainView2;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.atguigu.admin.mapper")
//@ServletComponentScan(basePackages = "com.atguigu.admin")
@SpringBootApplication
public class Boot05WebAdminApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
//        SpringApplication.run(Boot05WebAdminApplication.class, args);
        launch(Boot05WebAdminApplication.class, MainView.class, args);
    }

}
