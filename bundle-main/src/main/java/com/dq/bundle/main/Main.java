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
            InvocationTargetException
    {
//        loaderErrorTest();
        selfLoaderTest();
    }

    static void loaderErrorTest() throws
            MalformedURLException,
            ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            NoSuchMethodException,
            InvocationTargetException
    {
        Main m = new Main();
        String filePath1 = "/Users/mubi/IdeaProjects/bundle/demo1/target/demo1-1.0-SNAPSHOT.jar";
        URLClassLoader ul1 = new URLClassLoader(m.convert2URLArray(filePath1));

        String filePath2 = "/Users/mubi/IdeaProjects/bundle/demo2/target/demo2-1.0-SNAPSHOT.jar";
        URLClassLoader ul2 = new URLClassLoader(m.convert2URLArray(filePath2));

        /**
         * 正常加载Hello类
         */
        Class<?> helloClass = ul1.loadClass("com.dq.bundle.demo1.Hello");
        Object helloInstance = helloClass.newInstance();
        Method sayMethod = helloClass.getMethod("say", String.class);
        sayMethod.invoke(helloInstance, "world");

        /**
         * ul2 正常加载User类，但是无法加载其依赖的 Hello类，会报错ClassNotFoundException
         */
        Class<?> userClass = ul2.loadClass("com.dq.bundle.demo2.User");
        Object userInstance = userClass.newInstance();
        Method welcomeMethod = userClass.getMethod("welcome", helloClass);
        welcomeMethod.invoke(userInstance, helloInstance);
    }

    static void selfLoaderTest() throws
            MalformedURLException,
            ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            NoSuchMethodException,
            InvocationTargetException
    {
        Main m = new Main();
        String filePath1 = "/Users/mubi/IdeaProjects/bundle/demo1/target/demo1-1.0-SNAPSHOT.jar";
        URLClassLoader ul1 = new URLClassLoader(m.convert2URLArray(filePath1));

        String filePath2 = "/Users/mubi/IdeaProjects/bundle/demo2/target/demo2-1.0-SNAPSHOT.jar";
        SelfClassLoader ul2 = new SelfClassLoader(m.convert2URLArray(filePath2), ul1);

        /**
         * 正常加载Hello类
         */
        Class<?> helloClass = ul1.loadClass("com.dq.bundle.demo1.Hello");
        Object helloInstance = helloClass.newInstance();
        Method sayMethod = helloClass.getMethod("say", String.class);
        sayMethod.invoke(helloInstance, "world");

        /**
         *  ul2正常加载User类, 并使用传递过来的ul1正常加载Hello类
         */
        Class<?> userClass = ul2.loadClass("com.dq.bundle.demo2.User");
        Object userInstance = userClass.newInstance();
        Method welcomeMethod = userClass.getMethod("welcome", helloClass);
        welcomeMethod.invoke(userInstance, helloInstance);
    }

    /**
     * 自定义类加载器，可以传入其它类加载器
     */
    static class SelfClassLoader extends URLClassLoader {
        private ClassLoader parentClassLoader;

        public SelfClassLoader(URL[] urls, ClassLoader classLoader){
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

        public Class<?> loadFromParent(String name){
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

    private URL[] convert2URLArray(String filePath) throws MalformedURLException{
        File f = new File(filePath);
        URL[] urls = new URL[1];
        urls[0] = f.toURI().toURL();
        return urls;
    }
}
