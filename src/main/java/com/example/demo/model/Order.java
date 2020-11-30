package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_order")
public class Order {
    private Long id;
    private Long good_id;
    private Long user_id;
}
