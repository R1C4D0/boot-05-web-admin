package com.atguigu.admin.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("sc")
public class Sc {
    private String sId;
    private String cId;
    private Float score;
}
