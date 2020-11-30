package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_good")
public class Good {
    private Long id;
    private String goodname;
    private Long stock;
}
