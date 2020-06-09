package com.github.nmyphp;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.loader.ExecutableArchiveLauncher;
import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.Archive.Entry;
import org.springframework.boot.loader.archive.ExplodedArchive;
import org.springframework.boot.loader.archive.JarFileArchive;
import org.springframework.boot.loader.jar.JarFile;

public class PluginClassloaderFactory extends ExecutableArchiveLauncher {

    private static final String BOOT_INF_CLASSES = "BOOT-INF/classes/";
    private static final String BOOT_INF_LIB = "BOOT-INF/lib/";
    private String jarPath;

    public PluginClassloaderFactory setPath(String jarPath) {
        this.jarPath = "file:/" + jarPath;
        return this;
    }

    public ClassLoader build() {
        try {
            JarFile.registerUrlProtocolHandler();
            ClassLoader classLoader = createClassLoader(getClassPathArchives());
            Thread.currentThread().setContextClassLoader(classLoader);
            return classLoader;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    protected List<Archive> getClassPathArchives() throws Exception {
        Archive archive = this.createArchiveByPath(jarPath);
        List<Archive> archives = new ArrayList<>(archive.getNestedArchives(this::isNestedArchive));
        postProcessClassPathArchives(archives);
        return archives;
    }

    @Override
    protected boolean isNestedArchive(Entry entry) {
        if (entry.isDirectory()) {
            return entry.getName().equals(BOOT_INF_CLASSES);
        }
        return entry.getName().startsWith(BOOT_INF_LIB);
    }

    private Archive createArchiveByPath(String jarPath) throws Exception {
        URI location = new URI(jarPath);
        String path = (location != null) ? location.getSchemeSpecificPart() : null;
        if (path == null) {
            throw new IllegalStateException("Unable to determine code source archive");
        }
        File root = new File(path);
        if (!root.exists()) {
            throw new IllegalStateException(
                "Unable to determine code source archive from " + root);
        }
        return (root.isDirectory() ? new ExplodedArchive(root)
            : new JarFileArchive(root));
    }
}
