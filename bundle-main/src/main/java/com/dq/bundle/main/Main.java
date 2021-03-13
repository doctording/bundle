package com.dq.bundle.main;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @Author mubi
 * @Date 2021/3/13 10:47
 */
public class Main {
    public static void main(String[] args) throws
            MalformedURLException,
            ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            NoSuchMethodException,
            InvocationTargetException {
        Main m = new Main();
        String hello1Path = "/Users/mubi/IdeaProjects/bundle/demo2/src/main/lib/demo1-1.0-SNAPSHOT.jar";
        String hello2Path = "/Users/mubi/IdeaProjects/bundle/demo3/src/main/lib/demo1-2.0-SNAPSHOT.jar";
        String hello3Path = "/Users/mubi/IdeaProjects/bundle/demo1/target/demo1-3.0-SNAPSHOT.jar";
        URLClassLoader helloClassLoader1 = new URLClassLoader(m.convert2URLArray(hello1Path));
        URLClassLoader helloClassLoader2 = new URLClassLoader(m.convert2URLArray(hello2Path));
        URLClassLoader helloClassLoader3 = new URLClassLoader(m.convert2URLArray(hello3Path));


        /**
         * 正常加载Hello类 版本3
         */
        Class<?> helloClass3 = helloClassLoader3.loadClass("com.dq.bundle.demo1.Hello");
        Object helloInstance = helloClass3.newInstance();
        Method sayMethod = helloClass3.getMethod("say", String.class);
        sayMethod.invoke(helloInstance, "world");

        // User类使用 Hello 版本1
        String userPath = "/Users/mubi/IdeaProjects/bundle/demo2/target/demo2-3.0-SNAPSHOT.jar";
        SelfClassLoader userLoader = new SelfClassLoader(m.convert2URLArray(userPath),
                helloClassLoader1);

        // Teacher类使用 Hello 版本2
        String teacherPath = "/Users/mubi/IdeaProjects/bundle/demo3/target/demo3-3.0-SNAPSHOT.jar";
        SelfClassLoader teacherLoader = new SelfClassLoader(m.convert2URLArray(teacherPath),
                helloClassLoader2);


        /**
         *  正常加载User类, 并使用传递过来的 helloClassLoader1 正常加载Hello类
         */
        Class<?> helloClass1 = helloClassLoader1.loadClass("com.dq.bundle.demo1.Hello");
        Object helloInstance1 = helloClass1.newInstance();
        Class<?> userClass = userLoader.loadClass("com.dq.bundle.demo2.User");
        Object userInstance = userClass.newInstance();
        Method welcomeMethod = userClass.getMethod("welcome", helloClass1);
        welcomeMethod.invoke(userInstance, helloInstance1);

        /**
         * 正常加载Teacher类，并使用传递过来的 helloClassLoader2 正常加载Hello类
         */
        Class<?> helloClass2 = helloClassLoader2.loadClass("com.dq.bundle.demo1.Hello");
        Object helloInstance2 = helloClass2.newInstance();
        Class<?> teacherClass = teacherLoader.loadClass("com.dq.bundle.demo3.Teacher");
        Object teacherInstance = teacherClass.newInstance();
        Method welcomeTeacherMethod = teacherClass.getMethod("welcome", helloClass2);
        welcomeTeacherMethod.invoke(teacherInstance, helloInstance2);
    }

    /**
     * 自定义类加载器，可以传入其它类加载器
     */
    static class SelfClassLoader extends URLClassLoader {
        private ClassLoader parentClassLoader;

        public SelfClassLoader(URL[] urls, ClassLoader classLoader) {
            super(urls);
            this.parentClassLoader = classLoader;
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            Class<?> clazz = loadFromParent(name);
            if (clazz != null) {
                return clazz;
            }
            return super.loadClass(name);
        }

        public Class<?> loadFromParent(String name) {
            // 从当前classLoader加载
            Class<?> loadClass = super.findLoadedClass(name);

            try {
                if (loadClass == null) {
                    // 用父加载加载，这里是自己传入的类加载器
                    loadClass = this.parentClassLoader.loadClass(name);
                }
            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
            }
            return loadClass;
        }
    }

    private URL[] convert2URLArray(String filePath) throws MalformedURLException {
        File f = new File(filePath);
        URL[] urls = new URL[1];
        urls[0] = f.toURI().toURL();
        return urls;
    }
}
