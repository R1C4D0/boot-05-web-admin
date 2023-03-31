package com.atguigu.admin.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("student")
public class Student {
    private String sId;
    private String sName;
    private String sSex;
    private Integer sAge;
    private String sClass;


}
