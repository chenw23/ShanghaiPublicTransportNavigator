package com.fudan.sw.dsa.project2.constant;

import java.io.File;

/**
 * for constant value
 *
 * @author zjiehang
 */
public class FileGetter {
    private static final String FILE_PATH = "subway.txt";

    public File readFileFromClasspath() {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(FILE_PATH).getFile());
    }
}
