package com.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author yzzhang
 * @date 2020/9/9 0:00
 */
@Data
@TableName(value = "help_category")
public class HelpCategory {
    // @TableId() 修饰主键的
    @TableField(value = "help_category_id")
    private String helpCategoryID;
    private String name;
    @TableField(value = "parent_category_id")
    private String parentCategoryID;
    private String url;
}
