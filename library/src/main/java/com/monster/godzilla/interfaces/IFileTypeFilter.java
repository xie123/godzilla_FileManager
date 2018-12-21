package com.monster.godzilla.interfaces;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>统计文件类型的拦截器</ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/11/29/029
 */
public interface IFileTypeFilter extends CallBack{
    //类型归档
    String accept();

    /**
     * 判断是否是该类型
     * @param filePath
     * @return
     */
    boolean judge(String filePath);
}
