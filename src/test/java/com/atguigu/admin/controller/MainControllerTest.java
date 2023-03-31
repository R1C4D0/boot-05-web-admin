package com.atguigu.admin.controller;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {

    @Test
    void onSql() {
        String sql = "select * from student";
        String tableName = null;
        Pattern pattern = Pattern.compile("(?i)\\bFROM\\b\\s+([a-zA-Z_][a-zA-Z0-9_]*)");
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }
}