package com.monster.godzilla.config;


import com.monster.godzilla.interfaces.IFileTypeFilter;

import java.util.LinkedHashMap;

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
public abstract class FileType  implements IFileTypeFilter {
    private String type;
    /**
     * 额外参数
     */
    private LinkedHashMap<String,Object> additionalConfiguration;
    public FileType(String type) {
        this.type = type;
        additionalConfiguration=new LinkedHashMap<>();
    }

    @Override
    public boolean judge(String filePath) {
        return false;
    }

    @Override
    public String accept() {
        return null;
    }

    public String getType() {
        return type;
    }

    public Object getAdditionalConfiguration(String key) {
        return additionalConfiguration.get(key);
    }

    public void addConfiguration(String key, Object  value) {
        additionalConfiguration.put(key,value);
    }
}
