package com.monster.godzilla.manager.receiver;

import com.monster.godzilla.manager.lifecycle.LifecycleListener;

import java.util.Collection;

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
 * @date 2018/11/21/021
 */
public interface MountMonitor extends LifecycleListener {
    public interface MountListener {

        void postChangeAfter(Collection<String> path);

    }
}

