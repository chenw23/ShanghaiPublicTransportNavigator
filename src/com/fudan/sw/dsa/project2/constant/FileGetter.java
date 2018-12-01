package com.fudan.sw.dsa.project2.constant;

import java.io.File;
import java.net.URL;

/**
 * for constant value
 * @author zjiehang
 *
 */
public class FileGetter
{
	private static final String FILE_PATH = "subway.txt";

    public File readFileFromClasspath()
    {
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource(FILE_PATH).getFile());
        return file;
    }
}
