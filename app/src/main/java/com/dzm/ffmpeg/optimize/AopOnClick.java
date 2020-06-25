package com.dzm.ffmpeg.optimize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description TODO
 * @date 2020/6/17 16:40
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AopOnClick {
    /**
     * 点击间隔时间
     */
    long value() default 1000;
}
