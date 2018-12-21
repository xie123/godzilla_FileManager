package com.monster.godzilla.manager;

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
 * @date 2018/10/30/030
 */
public interface Request {


    void begin();

    boolean isRunning();

    void pause();

    boolean isPause();
    void cancel();

    boolean isCancelled();

    boolean isComplete();


    void clear();


}
