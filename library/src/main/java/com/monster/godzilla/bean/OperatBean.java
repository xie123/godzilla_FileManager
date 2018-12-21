package com.monster.godzilla.bean;

import java.io.Serializable;
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
 * @date 2018/12/14/014
 */
public class OperatBean implements Serializable {
    public String path;
    public List<String> operatPath;


    public Map<String /*文件归类类型*/,Set<String>> statisticClass;

    public long mOperateSize;
    public boolean isSuccess;
    public long fileCount;


}
