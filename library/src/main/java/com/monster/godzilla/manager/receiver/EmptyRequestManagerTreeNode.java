package com.monster.godzilla.manager.receiver;

import com.monster.godzilla.RequestManager;
import com.monster.godzilla.manager.RequestManagerTreeNode;

import java.util.Collections;
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
public class EmptyRequestManagerTreeNode  implements RequestManagerTreeNode {
    @Override
    public Set<RequestManager> getDescendants() {
        return Collections.emptySet();
    }
}
