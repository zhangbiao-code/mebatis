package com.biao.mebatis.v2.annotation;

import java.lang.annotation.*;

/**
 * 用于注解方法,配置SQL语句
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {
    String value();
}
