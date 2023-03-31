package com.atguigu.admin.controller;

import com.atguigu.admin.service.UserService;
import com.atguigu.admin.utils.SpringContextUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author NLER
 * @date 2022/12/6 23:22
 */
@FXMLController
public class TableController implements Initializable {
    @FXML
    private Button submitButton;

    @FXML
    private TextField helloText;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @FXML
    void onSubmit(ActionEvent event) {
        UserService userService = SpringContextUtil.getBean(UserService.class);
        String text = jdbcTemplate.queryForObject("select count(*) from address_book", Long.class).toString();
        helloText.setText(text);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
