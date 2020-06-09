package com.github.nmyphp;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by xuwengui on 2020/4/16.
 */
public class PluginUtil {

    /**
     * 从给定jar包中获取指定接口的实现类.
     *
     * @param clazz   .
     * @param jarFile .
     * @return .
     */
    public static List<Class> getAllClassByInterface(Class clazz, String jarFile)
        throws IOException, ClassNotFoundException {
        ArrayList<Class> list = new ArrayList<Class>();
        List<Class> allClass = getClassFromJar(jarFile);
        // 判断是否是一个接口
        if (clazz.isInterface()) {
            //循环判断路径下的所有类是否实现了指定的接口 并且排除接口类自己
            for (int i = 0; i < allClass.size(); i++) {
                // 判断是不是同一个接口
                // isAssignableFrom:判定此 Class 对象所表示的类或接口与指定的 Class
                // 参数所表示的类或接口是否相同，或是否是其超类或超接口
                if (clazz.isAssignableFrom(allClass.get(i))) {
                    if (!clazz.equals(allClass.get(i))) {
                        // 自身并不加进去
                        list.add(allClass.get(i));
                        System.out.println("PluginUtil:\t" + allClass.get(i).getName());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 从jar包读取所有的class文件名.
     *
     * @param jarName .
     */
    private static List<Class> getClassFromJar(String jarName) throws IOException, ClassNotFoundException {
        List<Class> fileList = new ArrayList<Class>();
        URL jarUrl = new URL("jar:file:" + jarName + "!/");
        //自己定义的classLoader类，把外部路径也加到load路径里，使系统去该路经load对象
        ClassLoader loader = new URLClassLoader(new URL[]{jarUrl});
        //ClassLoader loader = Thread.currentThread().getContextClassLoader();
        JarFile jarFile = new JarFile(new File(jarName));
        Enumeration<JarEntry> en = jarFile.entries(); // 枚举获得JAR文件内的实体,即相对路径
        int i = 0;
        while (en.hasMoreElements()) {
            i++;
            JarEntry jarEntry = en.nextElement();
            String entryName = jarEntry.getName();
            if (!entryName.endsWith(".class") || jarEntry.isDirectory()) {
                continue;
            }
            String fullClzName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
            try {
                Class clz = loader.loadClass(fullClzName);
                fileList.add(clz);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("classNum:" + i);
        return fileList;
    }

    /**
     * 判断一个类是否继承某个父类或实现某个接口.
     *
     * @param className   .
     * @param parentClazz .
     */
    private static boolean isChildClass(String className, Class parentClazz) {
        if (className == null) {
            return false;
        }
        Class clazz = null;
        try {
            clazz = Class.forName(className);
            if (Modifier.isAbstract(clazz.getModifiers())) {
                return false;
            }
            if (Modifier.isInterface(clazz.getModifiers())) {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return parentClazz.isAssignableFrom(clazz);
    }

    /**
     * 从jar获取某包下所有类.
     *
     * @param jarFile .
     * @return 类的完整名称.
     */
    private static List<String> getClassNameFromJar(JarFile jarFile) {
        List<String> myClassName = new ArrayList<String>();
        try {
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                //LOG.info("entrys jarfile:"+entryName);
                if (entryName.endsWith(".class")) {
                    entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                    myClassName.add(entryName);
                    //LOG.debug("Find Class :"+entryName);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("发生异常:" + ex.getMessage());
        }
        return myClassName;
    }


}
