package com.cellulam.core.utils;

import com.cellulam.core.exceptions.RuntimeIOException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author eric.li
 * @date 2022-06-06 16:51
 */
public abstract class FileUtils {
    public static String getExtension(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        return StringUtils.substringAfterLast(fileName, ".");
    }

    /**
     * will override the local file
     *
     * @param url
     * @param dir
     * @param fileName
     * @return
     */
    public static File downloadFile(String url, String dir, String fileName) {
        return downloadFile(url, dir, fileName, true);
    }

    /**
     * download file
     *
     * @param url
     * @param dir
     * @param fileName
     * @param override If false, the original file is taken directly if exists and not re-downloaded from the URL.
     * @return
     */
    public static File downloadFile(String url, String dir, String fileName, boolean override) {
        File file = new File(dir + fileName);

        if (override) {
            file.deleteOnExit();
        } else if (file.exists()) {
            return file;
        }

        try {
            URL httpUrl = new URL(url);
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            org.apache.commons.io.FileUtils.copyURLToFile(httpUrl, file);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
        return file;
    }

}
