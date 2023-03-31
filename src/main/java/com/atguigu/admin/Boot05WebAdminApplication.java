package com.atguigu.admin;

import com.atguigu.admin.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.atguigu.admin.mapper")
//@ServletComponentScan(basePackages = "com.atguigu.admin")
@SpringBootApplication
public class Boot05WebAdminApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
//        SpringApplication.run(Boot05WebAdminApplication.class, args);
        launch(Boot05WebAdminApplication.class, MainView.class, args);
    }

}
