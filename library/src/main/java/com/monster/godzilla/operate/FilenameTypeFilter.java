package com.monster.godzilla.operate;

import com.monster.godzilla.FileManager;
import com.monster.godzilla.utlis.FileManagerUtil;

import java.io.File;
import java.io.FileFilter;

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
 * @date 2018/12/3/003
 */
public class FilenameTypeFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        String[] split = pathname.getAbsolutePath().split(File.separator);

        boolean is=split.length <= 50;
        boolean is2= FileManagerUtil.isNormalFile(pathname.getAbsolutePath());
        if(is&&is2){
            if(!FileManager.init().getFileManagerConfiguration().isWhetherToReadHiddenFiles()){
                return !pathname.isHidden();
            }
            return true;
        }
        return false;
    }
}
