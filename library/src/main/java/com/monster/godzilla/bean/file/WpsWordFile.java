package com.monster.godzilla.bean.file;

import com.monster.godzilla.config.FileType;
import com.monster.godzilla.interfaces.IFileManagerClassify;
import com.monster.godzilla.interfaces.IFileManagerFileType;
import com.monster.godzilla.utlis.FileManagerFileTypeUtils;
import com.monster.godzilla.utlis.FileManagerFileUtils;

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
public class WpsWordFile extends FileType {

    public WpsWordFile() {
        super(IFileManagerFileType.WPS_WORD);
    }

    @Override
    public String accept() {
        return IFileManagerClassify.DOCUMENT;
    }
    private static final String[] extension=new String[]{"doc","docx"};
    @Override
    public boolean judge(String filePath) {
        String fileExtension = FileManagerFileUtils.getExtensionName(filePath);
        return FileManagerFileTypeUtils.isThisType(fileExtension,extension);
    }


}
