package com.fudan.sw.dsa.project2.constant;

import java.io.File;

/**
 * for constant value
 *
 * @author zjiehang
 */
public class FileGetter {
    public File readFileFromClasspath(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(path).getFile());
    }
}
