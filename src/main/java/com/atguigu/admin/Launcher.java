package com.atguigu.admin;


import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutionException;

/**
 * 用于启动JavaFX
 * @author SW-Fantastic
 */

public class Launcher {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Boot05WebAdminApplication application = new Boot05WebAdminApplication();
    }

}
