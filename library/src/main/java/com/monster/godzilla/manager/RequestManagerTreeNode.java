package com.monster.godzilla.manager;

import com.monster.godzilla.RequestManager;

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
 * @date 2018/10/29/029
 */
public interface RequestManagerTreeNode {
    /**
     * Returns all descendant {@link RequestManager}s relative to the context of the current {@link RequestManager}.
     */
    Set<RequestManager> getDescendants();
}
