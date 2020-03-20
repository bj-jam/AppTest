package com.app.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@MyAnTargetType
public class MainUtil {
    public static void main(String[] args) throws NoSuchMethodException, SecurityException {
//        Method method1 = MainUtil.class.getMethod("test");
//        MyAnnotation myAnnotation = method1.getAnnotation(MyAnnotation.class);
//        System.out.println(myAnnotation.str() + "----" + myAnnotation.val());

        // 获取类上的注解MyAnTargetType
//        MyAnTargetType t = MainUtil.class.getAnnotation(MyAnTargetType.class);
//        System.out.println("类上的注解值 === " + t.value());
//        MyAnTargetMethod tm = null;
//        try {
//            // 根据反射获取AnnotationTest类上的test方法
//            Method method = MainUtil.class.getDeclaredMethod("test", String.class);
//            // 获取方法上的注解MyAnTargetMethod
//            tm = method.getAnnotation(MyAnTargetMethod.class);
//            System.out.println("方法上的注解值 === " + tm.value());
//            // 获取方法上的所有参数注解  循环所有注解找到MyAnTargetParameter注解
//            Annotation[][] annotations = method.getParameterAnnotations();
//            for (Annotation[] tt : annotations) {
//                for (Annotation t1 : tt) {
//                    if (t1 instanceof MyAnTargetParameter) {
//                        System.out.println("参数上的注解值 === " + ((MyAnTargetParameter) t1).value());
//                    }
//                }
//            }
//            method.invoke(new MainUtil(), "改变默认参数");
//            // 获取AnnotationTest类上字段field的注解MyAnTargetField
//            MyAnTargetField fieldAn = MainUtil.class.getDeclaredField("field").getAnnotation(MyAnTargetField.class);
//            System.out.println("字段上的注解值 === " + fieldAn.value());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        dfsf();
    }

    private static void dfsf() {
        int i = 50000;
        System.out.println(i *0.0000001f);
    }

//    @MyAnnotation(str = "33", val = 11)
//    public static void test() {
//
//    }

    /**
     * 定义一个可以注解在PARAMETER上的注解
     *
     * @date 2018年4月22日
     */
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MyAnTargetParameter {
        /**
         * 定义注解的一个元素 并给定默认值
         *
         * @return
         */
        String value() default "我是定义在参数上的注解元素value的默认值";
    }


    @MyAnTargetField
    private String field = "我是字段";

    @MyAnTargetMethod("测试方法")
    public void test(@MyAnTargetParameter String args) {
        System.out.println("参数值 === " + args);
    }

}
