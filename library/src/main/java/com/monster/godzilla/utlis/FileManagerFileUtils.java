package com.monster.godzilla.utlis;

import com.monster.godzilla.FileManager;
import com.monster.godzilla.bean.FileManagerFileInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>无</ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/11/22/022
 */
public class FileManagerFileUtils {
    public static final String LOG_TAG = FileManagerFileUtils.class.getName();
    /**
     * 获取文件名
     * @param path
     * @return
     */
    public static String getFileName(String path) {

        if (path == null || path.length() <= 0) {
            return path;
        }
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 获取 扩展名
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {

        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return null;
    }


    public static String getNewFolderPath(String oldPath) {
        String path = oldPath + "-副本";
        if (new File(path).exists()) {
            path = getNewFolderPath(path);
        }
        return path;
    }



    /**
     * 复制文件
     *
     * @param src    需要复制的文件
     * @param target 目标文件
     */
    public static String copyFile(File src, File target) {

        if (src == null || target == null) {
            return null;
        }

        if (src.isDirectory()) {
            //不予许复制到子目录
            if (target.getAbsolutePath().startsWith(src.getAbsolutePath() + File.separator)) {
                return null;
            }
            if (!target.exists()) {
                boolean mkdirs = target.mkdirs();
                if (!mkdirs) {
                    return null;
                }
            }
            // 复制文件夹
            File[] currentFiles=FileManagerFileUtils.fileFilter(src);
            if (currentFiles != null) {
                for (File currentFile : currentFiles) {
                    // 如果当前为子目录则递归
                    if (currentFile.isDirectory()) {
                        copyFile(new File(currentFile + "/"), new File(target.getAbsolutePath() + "/" + currentFile.getName() + "/"));
                    } else {
                        copyFile(currentFile, new File(target.getAbsolutePath()  + "/" + currentFile.getName()));
                    }
                }
            }
            return target.exists() ? target.getAbsolutePath() : null;

        } else {

            FileChannel input = null;
            FileChannel output = null;

            try {
                input = new FileInputStream(src).getChannel();
                output = new FileOutputStream(target).getChannel();
                output.transferFrom(input, 0, input.size());
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return target.exists() ? target.getAbsolutePath() : null;
        }
    }


    public static File getNewFile(File dir, File srcFile, File targetFile, AtomicInteger atomicInteger) {
        File[] files = dir.listFiles();
        if (files != null) {
            atomicInteger.incrementAndGet();
            for (File file : files) {
                if (file.getName().equals(targetFile.getName())) {
                    if (srcFile.isDirectory()) {
                        targetFile = new File(srcFile.getName() + "(" + atomicInteger.get() + ")");
                    } else {
                        String[] tmp_name = srcFile.getName().split("\\.");
                        String newName = "";
                        if (tmp_name.length == 0) {
                            newName = srcFile.getName();
                            targetFile = new File(newName + "(" + atomicInteger.get() + ")");
                        } else {
                            for (int k = 0; k < tmp_name.length; k++) {
                                if (tmp_name.length > 1) {
                                    if (k == tmp_name.length - 2) {
                                        tmp_name[k] = tmp_name[k] + "(" + atomicInteger.get() + ")";
                                    }
                                    if (k == tmp_name.length - 1) {
                                        newName += tmp_name[k];
                                    } else {
                                        newName += tmp_name[k] + ".";
                                    }
                                } else {
                                    newName = tmp_name[k] + "(" + atomicInteger.get() + ")";
                                }
                            }
                            targetFile = new File(newName);
                        }

                    }
                    if (!targetFile.renameTo(targetFile)
                            && isSameFile(dir, targetFile)) {
                        getNewFile(dir, srcFile, targetFile, atomicInteger);
                    }

                }
            }
            targetFile = new File(dir, targetFile.getName());
        }

        // L.Debug("dir.getPath():"+dir.getPath());
        // L.Debug("targetFile.getPath():"+targetFile.getPath());
        return targetFile;
    }

    private static boolean isSameFile(File dir, File targetFile) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(targetFile.getName())) {
                return true;
            }
        }
        return false;
    }





    /**
     * 文件过滤,将手机中隐藏的文件给过滤掉
     */
    public static File[] fileFilter(File file) {
        return file.listFiles(FileManager.init().getFileManagerConfiguration().getFilenameFilter());
    }


    /**
     * 获取文件信息
     * @return
     */
    public static FileManagerFileInfo getFileInfoFromFile(File file) {
        FileManagerFileInfo fileInfo = new FileManagerFileInfo();
        String filePath = file.getAbsolutePath();
        fileInfo.setFileName(FileManagerFileUtils.getFileName(filePath));
        fileInfo.setPath(filePath);
        fileInfo.setTime(getFileLastModifiedTime(file));
        fileInfo.setTimestamp( file.lastModified());
        fileInfo.setFileType(FileManager.init().getDiskInfoManager().getFileTypeByPath(file));
        fileInfo.setDir(file.isDirectory());
        fileInfo.setFileSize(file.length());
        fileInfo.setUpperLevelDirectory(file.getParent());
        fileInfo.setCanRead(file.canRead());
        fileInfo.setExtensionName(FileManagerFileUtils.getExtensionName(filePath));
        fileInfo.setCanWrite(file.canWrite());
        fileInfo.setHidden(file.isHidden());
        if (file.isDirectory()) {
            File[] files =fileFilter(file);
            fileInfo.setChildFileNum(files==null?0:files.length);
        } else {

        }
        return fileInfo;
    }


    /**
     * 获取指定路径文件的总大小
     *
     * @param files
     * @return
     */
    public static long getFileSize(List<String> files) {
        long size = 0;
        for (int i = 0; i < files.size(); i++) {
            size += FileUtils.getDirLength(files.get(i));
        }

        return size;
    }



    // 递归
    public static  Long[] getFileSize(File file) {
        Long[] data=new Long[2];
        long size = 0;
        long fileCount=0;
        if (file.isFile()) {
            fileCount++;
            size += file.length();
            data[0]=size;
            data[1]=fileCount;
            return  data;
        } else if (file.isDirectory()) {
            File[] files = fileFilter(file);
            if (files != null) {
                for (File file1 : files) {
                    Long[] data2= getFileSize(file1);
                    size = size + data2[0];
                    fileCount = fileCount + data2[0];
                    data[0] = size;
                    data[1] = fileCount;
                }
            }
        }
        return data;
    }

    public static String getNewFileName(String newPath) {
        String filePath = null;

        int index = newPath.lastIndexOf(".");
        if (index == -1 || index == 0 || (index == newPath.length() - 1)) {
            filePath = newPath + "-副本";
            if (new File(filePath).exists()) {
                filePath = getNewFileName(filePath);
            }
        } else {
            filePath = newPath.substring(0, index) + "-副本" + newPath.substring(index);
            if (new File(filePath).exists()) {
                filePath = getNewFileName(filePath);
            }
        }
        return filePath;

    }


    /**
     * 读取文件的最后修改时间的方法
     */
    public static String getFileLastModifiedTime(File f) {
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }


}
