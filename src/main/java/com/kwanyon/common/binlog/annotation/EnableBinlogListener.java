package com.kwanyon.common.binlog.annotation;

import com.kwanyon.common.binlog.config.BinlogAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Binlog 监听器启动开关
 *
 * @author Alay
 * @since 2022-11-14 16:24
 */
@Documented
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Import(value = {BinlogAutoConfiguration.class})
public @interface EnableBinlogListener {

}
