package com.monster.godzilla.utlis;

import java.io.File;

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
 * @date 2018/12/13/013
 */
public class SDPermission {
    public SDPermission() {
    }

    public static boolean checkFsWritable(String diskPath) {
        String directoryName = diskPath + "/DCIM";
        File directory = new File(directoryName);
        if (!directory.isDirectory() && !directory.mkdirs()) {
            return false;
        } else {
            File f = new File(directoryName, ".probe");

            try {
                if (f.exists()) {
                    f.delete();
                }

                if (!f.createNewFile()) {
                    return false;
                } else {
                    f.delete();
                    return true;
                }
            } catch (Exception var4) {
                return false;
            }
        }
    }
}
