package com.monster.godzilla.operate;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashSet;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>文件过滤</ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/12/3/003
 */
public class FilenameExtFilter implements FilenameFilter {

    private HashSet<String> mExts = new HashSet<String>();

    // using lower case
    public FilenameExtFilter(String[] exts) {
        if (exts != null) {
            mExts.addAll(Arrays.asList(exts));
        }
    }

    public boolean contains(String ext) {
        return mExts.contains(ext.toLowerCase());
    }

    @Override
    public boolean accept(File dir, String filename) {

        File file = new File(dir + File.separator + filename);
        if (file.isDirectory()) {
            return true;
        }

        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            String ext = (String) filename.subSequence(dotPosition + 1, filename.length());
            return contains(ext.toLowerCase());
        }

        return false;
    }
}
