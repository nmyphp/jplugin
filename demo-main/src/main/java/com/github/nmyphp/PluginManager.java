package com.github.nmyphp;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginManager {

    private static Map<String, Object> instancesMap = new ConcurrentHashMap<>();

    public static <T> T load(String jar, String className) {
        try {
            String key = md5(jar, className);
            if (instancesMap.containsKey(key)) {
                return (T) instancesMap.get(key);
            }
            URL[] url = new URL[1];
            url[0] = new URL("file:" + jar);
            URLClassLoader classLoader = new URLClassLoader(url);
            Class<?> clazz = classLoader.loadClass(className);
            T instance = (T) clazz.newInstance();
            instancesMap.put(key, instance);
            Thread.currentThread().setContextClassLoader(classLoader);
            return instance;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String md5(String jarPath, String className) {
        String key = jarPath + "@" + className;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes("UTF-8"));
            byte[] bytes = digest.digest();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                builder.append(Integer.toHexString((0x000000FF & bytes[i]) | 0xFFFFFF00).substring(6));
            }
            return builder.toString();
        } catch (Exception ex) {
            return key;
        }
    }
}
