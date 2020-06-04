package com.app.test.hook;//package com.app.test.hook;
//
//import android.app.Activity;
//import android.app.Instrumentation;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.IBinder;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
///**
// * @author lcx
// * Created at 2020.3.18
// * Describe:
// */
//public class InstrumentationProxy extends Instrumentation {
//    protected Instrumentation instrumentation;
//
//    public InstrumentationProxy(Instrumentation instrumentation) {
//        this.instrumentation = instrumentation;
//    }
//
//    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
//                                            Intent intent, int requestCode, Bundle options) {
//        //在启动Activity的时候，在Intent里面传入一个假的已经注册的Activity的ComponentName
//        ComponentName fakeComponentName = intent.getParcelableExtra("fakeComponentName");
//        if (fakeComponentName != null) {
//            intent.putExtra("realComponentName", intent.getComponent());
//            intent.setComponent(fakeComponentName);
//        }
//        Class[] classes = new Class[]{Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class};
//        try {
//            Method method = getClass().getSuperclass().getDeclaredMethod("execStartActivity", classes);
//            if (method != null) {
//                //注意这里的obj必须用原本的Instrumentation
//                return (ActivityResult) method.invoke(instrumentation, new Object[]{who, contextThread, token, target, intent, requestCode, options});
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
//        //拿到真实的ComponentName
//        ComponentName componentName = intent.getParcelableExtra("realComponentName");
//        if (componentName != null) {
//            className = componentName.getClassName();
//        }
//        return instrumentation.newActivity(cl, className, intent);
//    }
//}
