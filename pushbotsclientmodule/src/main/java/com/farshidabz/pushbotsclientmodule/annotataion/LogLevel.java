package com.farshidabz.pushbotsclientmodule.annotataion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Farshid since 15 Oct 2019
 *
 * LogLevel to handle log messages
 */

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface LogLevel {
    String DEBUG = "Debug";
    String RELEASE = "Release";
    String NONE = "Note";
}
