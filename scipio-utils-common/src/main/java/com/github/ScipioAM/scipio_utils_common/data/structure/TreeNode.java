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
    <T extends TreeNode> List<T> getChildren();

    /**
     * 添加子节点
     * @param childNode 子节点
     */
    void addChildNode(TreeNode childNode);

}
