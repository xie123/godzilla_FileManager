package com.monster.godzilla.bean.file;

import com.monster.godzilla.config.FileType;
import com.monster.godzilla.interfaces.IFileManagerClassify;
import com.monster.godzilla.interfaces.IFileManagerFileType;

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
public class NoneFile extends FileType {

    public NoneFile() {
        super(IFileManagerFileType.NONE);
    }

    @Override
    public String accept() {
        return IFileManagerClassify.NONE;
    }

    @Override
    public boolean judge(String filePath) {
        return true;
    }
}
