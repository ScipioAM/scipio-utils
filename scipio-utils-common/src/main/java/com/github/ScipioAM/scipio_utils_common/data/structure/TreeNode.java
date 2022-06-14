package com.github.ScipioAM.scipio_utils_common.data.structure;

import java.util.List;

/**
 * 树型结构的节点定义
 * @author Alan Scipio
 * @since 2021/4/23
 */
public interface TreeNode {

    /**
     * 节点id
     */
    Object getId();

    /**
     * 父节点id
     */
    Object getParentId();

    /**
     * 子节点集合
     */
    List<TreeNode> getChildren();

    /**
     * 添加子节点
     * @param childNode 子节点
     */
    void addChildNode(TreeNode childNode);

    /**
     * 获取节点的数据
     * @param <T> 节点数据的类型
     * @return 节点的数据
     */
    default <T> T getValue() {
        return null;
    }

}
