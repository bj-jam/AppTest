package com.app.test.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
annotation class MyAnnotation(val str: String, val `val`: Int)