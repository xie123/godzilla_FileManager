package com.monster.godzilla.bean.file;

import com.monster.godzilla.config.FileType;
import com.monster.godzilla.interfaces.IFileManagerClassify;
import com.monster.godzilla.interfaces.IFileManagerFileType;

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
 * @date 2018/12/6/006
 */
public class FolderFile extends FileType {

    public FolderFile() {
        super(IFileManagerFileType.FOLDER);
    }

    @Override
    public String accept() {
        return IFileManagerClassify.FOLDER;
    }

    @Override
    public boolean judge(String filePath) {
        File file =new File(filePath);
        boolean is=file.isDirectory();
        file=null;
        return is;
    }
}
