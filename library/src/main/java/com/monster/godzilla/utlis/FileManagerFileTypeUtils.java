package com.monster.godzilla.utlis;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.monster.godzilla.FileManager;
import com.monster.godzilla.config.FileType;
import com.monster.godzilla.bean.TypeConstant;
import com.monster.godzilla.interfaces.IFileManagerFileType;

import java.io.File;

/**
 */

public class FileManagerFileTypeUtils {

    public static FileType getFileType(File file) {
        if (file == null) {
            return FileManager.init().getDiskInfoManager().getFileTypeByType(IFileManagerFileType.NONE);
        }
        if (file.isDirectory()) {
            return FileManager.init().getDiskInfoManager().getFileTypeByType(IFileManagerFileType.FOLDER);
        }
        return FileManager.init().getDiskInfoManager().getFileTypeByPath(file);
    }

    public static FileType getFileType(String filePath) {
        if (filePath == null) {
            return FileManager.init().getDiskInfoManager().getFileTypeByType(IFileManagerFileType.NONE);
        }
        return getFileType(new File(filePath));
    }


    /**
     * 获取文件类型
     */
    @SuppressLint("DefaultLocale")
    public  static String getMIMEType(String filePath) {

        String type = "*/*";
        int dotIndex = filePath.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

        String end = filePath.substring(dotIndex, filePath.length()).toLowerCase();
        if ("".equals(end)) {
            return type;
        }

        for (String[] aMIME_MapTable : TypeConstant.MIME_MapTable) {
            if (end.equals(aMIME_MapTable[0])) {
                type = aMIME_MapTable[1];
            }
        }
        return type;
    }

    /**
     * 判断是否存在
     * @param extension
     * @param type
     * @return
     */
    public static boolean isThisType(String extension ,@NonNull String[] type){
        if (extension == null) {
            return false;
        }
        for (String classsify :type){
            if(extension.toLowerCase().equals(classsify)){
                return true;
            }
        }
        return false;
    }


}
