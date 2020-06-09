package com.github.nmyphp;


import com.github.nmyphp.interfaces.Collector;
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {
        String jar = "E:\\gitlocal\\demo-plugin\\demo-main\\src\\main\\resources\\plugins\\kafka-collector-1_0_x-1"
            + ".0-SNAPSHOT-jar-with-dependencies.jar";
        String className = "com.github.nmyphp.KafkaColletor";

        Collector collector = PluginManager.load(jar, className);
        List<String> tables = collector.listTable();
        System.out.println(tables);


        String jar2 = "E:/gitlocal/demo-plugin/demo-main/src/main/resources/plugins"
            + "/kafka-collector-0_10_x-1.0-SNAPSHOT.jar";
        ClassLoader loader = new PluginClassloaderFactory().setPath(jar2).build();
        Class<?> clazz = loader.loadClass(className);
        Collector collector2 = (Collector) clazz.newInstance();

        List<String> tables2 = collector.listTable();
        System.out.println(tables2);
    }
}
