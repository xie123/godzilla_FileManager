package com.monster.godzilla.utlis;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import com.monster.godzilla.bean.FileManagerDiskInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by $xuzhili on 2017/2/25.
 */

public final class FileManagerSdcardUtils {

    public static final long SIZE_KB = 1024L;
    public static final long SIZE_MB = SIZE_KB * SIZE_KB;
    public static final long SIZE_GB = SIZE_KB * SIZE_MB;

    public static final long TIME_SIZE_SECOND = 1000;
    public static final long TIME_SIZE_MIN =  60 * 1000;
    public static final long TIME_SIZAE_HOUR = 60 * 60 * 1000;

    public static final long MEMORY_4G = 4 * SIZE_GB;
    public static final long MEMORY_8G = 8 * SIZE_GB;
    public static final long MEMORY_16G = 16 * SIZE_GB;
    public static final long MEMORY_32G = 32 * SIZE_GB;


    /**
     * 获取本地磁盘路径
     * @return
     */
    public static String getLocalSDCardPath() {
        File sdDir = null;
        boolean sdCardExist = isSdExist(); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        } else {//若无sd卡则获取跟目录
            sdDir = Environment.getDataDirectory();//获取机身储存路径
        }
        if (sdDir != null) {
            return sdDir.getAbsolutePath();
        }
        return null;

    }

    /**
     * 磁盘是否存在
     * @return
     */
    public static boolean isSdExist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static void s(){
        File storage = new File("/storage");
        File[] files = storage.listFiles();
        for (final File file : files) {
            if (file.canRead()) {
                if(!isSdExist()||!file.getName().equals(Environment.getExternalStorageDirectory().getName())){
                    Log.d("sss","发现USB设备------------------>>>"+file.getAbsolutePath());
                }
            }
        }
    }
    public static void s1(){


    }
    /**
     * 获取本地外挂设备，包括sd卡。忆典u盘sd:/mnt/internal_sd---disk info:/mnt/internal_sd;/mnt/usb_storage/USB_DISK1/udisk0;
     *
     * @return
     */
    public  synchronized static HashSet<String> getAllSDPath() {
        s1();
        HashSet<String> paths = new HashSet<>();
        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
//                Log.d("FileManagerSdcardUtils", "line----" + line);
                if (line.contains("secure")
                        || line.contains("asec") || line.contains("legacy")
                        || line.contains("shell") || line.contains("private")
                        || line.contains("obb") || line.contains("media")
                        || line.contains("smb") || line.contains("Boot0loader")
                        || line.contains("Reserve")
                        || line.contains("runtime")
                        || line.contains("bootloader")
                        || line.contains("storage/emulated")) {
                    continue;
                }
                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns.length > 1) {
                        String column = columns[1];
//                        Log.d("findUsb", "findUsb--" + column);
                        if (column != null && column.contains("/")) {
                            Log.d("sss","发现USB设备------------------>>>"+column);
                            paths.add(column);
                        }
                    }
                }
                /*
                 * /dev/fuse /storage/udisk0 fuse rw,nosuid,nodev,noexec,relatime,user_id=1023,group_id=1023,default_permissions,allow_other 0 0
                 */

                else if (line.contains("/dev/fuse") ){
//                       && (line.contains("/storage/udisk") || line.contains("/storage/usbotg")))
                    if(line.contains("/storage/")){
                        if(!line.contains("/storage/emulated")){
                            String columns[] = line.split(" ");
                            if (columns.length > 1) {
                                String column = columns[1];

                                Log.d("sss","发现USB设备------------------>>>"+column);
                                paths.add(column);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return paths;
    }

    /**
     *      * 6.0获取
     *      * @param mContext
     *      * @return
     *     
     */

    public static List<String> getTheFinalPathOfTheUDisk(Context con) {
        String[] paths = null;
        List<String> data = new ArrayList<>();
        // include sd and usb devices
        StorageManager storageManager = (StorageManager) con.getSystemService(Context.STORAGE_SERVICE);
        try {
            paths = (String[]) StorageManager.class.getMethod("getVolumePaths").invoke(storageManager);

            for (String path : paths) {
                String state = (String) StorageManager.class.getMethod("getVolumeState", String.class).invoke(storageManager, path);
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    data.add(path);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }



    /**
     *      if (!usbBean.islocalUsb && !FileManager.init().getDiskInfoManager().contains(usbBean.path)) {
     *             return null;
     *         }
     * @param path
     * @param isGetCategory 是否 获取类别
     * @return 1.数据可能不准确，需要验证本地的磁盘 路径
     */
    public static FileManagerDiskInfo getSDCardInfo(String path, boolean isGetCategory) {

        if (path == null) {
            return null;
        }
        File sdDir = new File(path);
        if (!sdDir.exists()) {
            Log.d("FileManagerSdcardUtils", "localSDCardPathNotExists:" + path);
            return null;
        }

        long start = System.currentTimeMillis();
        FileManagerDiskInfo usbBean = new FileManagerDiskInfo();

        usbBean.path = sdDir.getAbsolutePath();
        if(sdDir.canWrite()){
            usbBean.canWrite= SDPermission.checkFsWritable(usbBean.path);
        }
        usbBean.canRead=sdDir.canRead();
        String localSDCardPath = getLocalSDCardPath();
        if (path.equals(localSDCardPath)) {
            usbBean.islocalUsb = true;
            usbBean.name = "本地磁盘";
        } else {
            usbBean.islocalUsb = false;
            usbBean.name = sdDir.getName();
        }
        if (isGetCategory && sdDir.isDirectory()) {
            while (sdDir.listFiles() == null && System.currentTimeMillis() - start < 6000) {
                Log.d("FileManagerSdcardUtils", "files is null:" + sdDir.getAbsolutePath());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            FileManagerScanUtil.scanFindDirectory(usbBean, sdDir);
        }

        long[] diskInfo;
        if (usbBean.islocalUsb) {
            diskInfo = getModifyTotalSize();
        } else {
            diskInfo = getDiskInfo(path, new long[2]);
        }
        usbBean.sizeTotal = diskInfo[0];
        usbBean.sizeAvailable = diskInfo[1];
        usbBean.sizeUsed = diskInfo[0] - diskInfo[1];

        if (usbBean.sizeTotal <= 0) {
            return null;
        }
        return usbBean;
    }

    /**
     * 获取本地磁盘 磁盘大小
     * @return
     */
    public static long[] getLocalStorage() {
        return getDiskInfo(FileManagerSdcardUtils.getLocalSDCardPath(), new long[2]);
    }

    /**
     * 获取磁盘的容量信息
     */
    @SuppressWarnings("unused")
    public static long[] getDiskInfo(String path, long[] size) {
        if (path == null) {
            return size;
        }
        if (path.equals(FileManagerSdcardUtils.getLocalSDCardPath())) {
            StatFsCompat statFsCompat=  new StatFsCompat(path);
            long blockSize = statFsCompat.getBlockSizeLong();
            long totalBlocks = statFsCompat.getBlockCountLong();
            long availBlocks = statFsCompat.getAvailableBlocksLong();
            size[0] = blockSize * totalBlocks;
            size[1] = blockSize * availBlocks;
            return size;
        } else {
            if (!TextUtils.isEmpty(path)) {
                File files = new File(path);
                long mTotalSize = getTotalSpace(files);//files.getTotalSpace();
                long mAvailSize = files.getFreeSpace();
//                Log.d("FileManagerSdcardUtils", "mTotalSize 1== 0:" + (mTotalSize == 0));
                if (mTotalSize == 0) {
                    File f0[] = files.listFiles();
                    if (f0 != null && f0.length > 0 && f0[0] != null) {
                        mTotalSize = getTotalSpace(f0[0]);//f0[0].getTotalSpace();
                        mAvailSize = f0[0].getFreeSpace();
                        if (mTotalSize == 0) {
                            File f1[] = f0[0].listFiles();
                            if (f1 != null && f1.length > 0 && f1[0] != null) {
                                mTotalSize = getTotalSpace(f1[0]);//f1[0].getTotalSpace();
                                mAvailSize = f1[0].getFreeSpace();
                            }
                        }
                    }
                }
                size[0] = mTotalSize;
                size[1] = mAvailSize;
                return size;
            }
        }

        return size;
    }

    /**
     * Returns the total size in bytes of the partition containing this path.
     * Returns 0 if this path does not exist.
     *
     * @param path 文件
     * @return -1 means path is null, 0 means path is not exist.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static long getTotalSpace(File path) {
        if (path == null) {
            return -1;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getTotalSpace();
        } else {// implements getTotalSpace() in API lower than GINGERBREAD
            if (!path.exists()) {
                return 0;
            } else {
                final  StatFsCompat statFsCompat=  new StatFsCompat((path.getPath()));
                // Using deprecated method in low API level system,
                // add @SuppressWarnings("description") to suppress the warning
                return statFsCompat.getBlockSizeLong() * statFsCompat.getBlockCountLong();
            }
        }
    }



    /**
     * 获取修正后总的存储的的值
     * long[0] totalSize long[1] availableSize
     *
     * @return
     */
    public static long[] getModifyTotalSize() {
        long[] localStorage = getLocalStorage();

        long size = localStorage[0];
        if (size <= MEMORY_4G) {
            size = MEMORY_4G;
        } else if (size <= MEMORY_8G) {
            size = MEMORY_8G;
        } else if (size <= MEMORY_16G) {
            size = MEMORY_16G;
        } else if (size <= MEMORY_32G) {
            size = MEMORY_32G;
        } else {
            size = size;
        }
        localStorage[0] = size;
        return localStorage;
    }
    public static String getSizeSting(long size) {

        if (size < SIZE_KB) {
            return size + "B";
        }

        if (size < SIZE_MB) {
            return Math.round(size * 100.0 / SIZE_KB) / 100.0 + "KB";
        }

        if (size < SIZE_GB) {
            return Math.round(size * 100.0 / SIZE_MB) / 100.0 + "MB";
        }

        return Math.round(size * 100.0 / SIZE_GB) / 100.0 + "G";

    }

}
