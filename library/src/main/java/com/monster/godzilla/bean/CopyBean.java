package com.monster.godzilla.bean;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class CopyBean {
    public int fileCount;
    /**
     * 复制出错的文件或者文件夹
     */
    public List<String> mCopyThePathOfTheError;
    /**
     * 复制 的 路径
     */
    public List<String> mSourceFiles;
    public String mDestinationPath;
    public long mCopyedSize;

    public Map<String /*文件归类类型*/,Set<String>> statisticClass;

}
