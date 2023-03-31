package com.atguigu.admin.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("course")
public class Course {
    private String cId;
    private String cName;
    private Float credit;
    private Integer cHours;
    private String tId;
}
