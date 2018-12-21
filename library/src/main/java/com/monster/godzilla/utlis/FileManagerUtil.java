package com.monster.godzilla.utlis;

import android.os.Looper;

import com.monster.godzilla.bean.GlobalConsts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
 * @date 2018/11/21/021
 */
public class FileManagerUtil {
    private static String ANDROID_SECURE = "/mnt/sdcard/.android_secure";



    public static <T> List<T> getSnapshot(Collection<T> other) {
        // toArray creates a new ArrayList internally and this way we can guarantee entries will not
        // be null. See #322.
        List<T> result = new ArrayList<T>(other.size());
        for (T item : other) {
            result.add(item);
        }
        return result;
    }
    public static void assertMainThread() {
        if (!isOnMainThread()) {
            throw new IllegalArgumentException("You must call this method on the main thread");
        }
    }

    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }

    public static boolean isOnMainThread() {

        return Looper.myLooper() == Looper.getMainLooper();
    }


    public static void add777(File file) {
        chmod("chmod " + "777" + " " + file.getParentFile().getAbsolutePath());
        chmod("chmod " + "777" + " " + file.getAbsolutePath());
//        if (!sdcardExit() || !sdCardPer()) {
//
//        }
    }
    private static void chmod(String cmd) {
        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  拼接路径
     * @param path1
     * @param path2
     * @return
     */
    public static String makePath(String path1, String path2) {
        if (path1.endsWith(File.separator)) {
            return path1 + path2;
        }

        return path1 + File.separator + path2;
    }

    /**
     * path1 是 path2 的 父路径
     * @param path1
     * @param path2
     * @return
     */

    public static boolean containsPath(String path1, String path2) {
        String path = path2;
        while (path != null) {
            if (path.equalsIgnoreCase(path1)) {
                return true;
            }
            if (path.equals(GlobalConsts.ROOT_PATH)) {
                break;
            }
            path = new File(path).getParent();
        }

        return false;
    }


    public static boolean isNormalFile(String fullName) {
        return !fullName.equals(ANDROID_SECURE);
    }





}
