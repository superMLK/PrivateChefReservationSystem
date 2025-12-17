package com.example.privatechefreservationsystem.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 只能用在方法上
@Retention(RetentionPolicy.RUNTIME) // 執行時期有效
public @interface LogTrace {
    String value() default ""; // 可以傳入自訂描述，例如 "取消訂位"
}
