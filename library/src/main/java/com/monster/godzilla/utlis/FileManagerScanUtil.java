package com.monster.godzilla.utlis;

import com.monster.godzilla.bean.FileManagerDiskInfo;
import com.monster.godzilla.config.FileType;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 扫描 信息 指定路径的 信息
 *
 * scanFindDirectory
 */

public final class FileManagerScanUtil {

    public static final ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public static void scanFindDirectory(FileManagerDiskInfo usbBean, File file) {
        findDirectory(usbBean, file);
        while (true) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (executorService.getActiveCount() == 0) {
                break;
            }
        }
    }

    public static void findDirectory(FileManagerDiskInfo usbBean, File file) {

        if (file != null) {
            if(file.isDirectory()){
                File[] files =  FileManagerFileUtils.fileFilter(file);

                if (files != null) {
                    int fileCount = 0;
                    for (File file1 : files) {
                        if (file1.isFile()) {
                            fileCount++;
                            findFile(file1,usbBean);
                        } else {
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    findDirectory(usbBean, file1);
                                }
                            });
                        }
                    }
                    usbBean.addNumberOfFilesUnderTheFolder(file.getAbsolutePath(),files.length);
                    usbBean.increaseFileCount(fileCount);
                }
            }else{
                findFile(file,usbBean);
                usbBean.addNumberOfFilesUnderTheFolder(file.getAbsolutePath(),1);
                usbBean.increaseFileCount(1);
            }

        }
    }

    private static void findFile(File file1, FileManagerDiskInfo usbBean) {
        FileType fileType = FileManagerFileTypeUtils.getFileType(file1.getAbsolutePath());
        String classify=fileType.accept();
        usbBean.addStatisticClass(classify,file1.getAbsolutePath());

    }
}

