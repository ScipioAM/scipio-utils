package com.github.ScipioAM.scipio_utils_common.data.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树型结构工具类
 * 输入输出的TreeNode实现类，必须完全一致，否则IDE不会报错但是实际运行报错
 *
 * @author Alan Scipio
 * @since 2021/4/23
 */
public class TreeStructureUtil {

    /**
     * 【根节点方式】创建树型结构
     *
     * @param root     根节点
     * @param nodeList 原始数据
     * @return 带有完整树型结构的根节点
     */
    public static TreeNode buildTreeForRoot(TreeNode root, List<TreeNode> nodeList) {
        //创建缓存
        Map<Object, TreeNode> cacheMap = new HashMap<>();
        //创建根节点
        cacheMap.put(root.getId(), root);
        //遍历原始数据list，构建树型结构
        nodeList.forEach(currentNode -> checkCache(cacheMap, currentNode));
        cacheMap.clear();//清除缓存
        return root;
    }

    /**
     * 【森林方式】创建树型结构
     *
     * @param rootNodeId 根节点的id
     * @param nodeList   原始数据
     * @return 森林（没有根节点）
     */
    public static <T extends TreeNode> List<T> buildTreeForList(Object rootNodeId, List<T> nodeList) {
        //创建结果集
        List<T> resultList = new ArrayList<>();
        //创建缓存
        Map<Object, TreeNode> cacheMap = new HashMap<>();
        //遍历原始数据list，构建树型结构
        nodeList.forEach(currentNode -> {
            if (currentNode.getParentId().equals(rootNodeId)) {
                resultList.add(currentNode);
            }
            checkCache(cacheMap, currentNode);
        });
        cacheMap.clear();//清除缓存
        return resultList;
    }

    /**
     * 创建树型结构，遍历时的工作
     */
    private static void checkCache(Map<Object, TreeNode> cacheMap, TreeNode currentNode) {
        TreeNode parentNode = cacheMap.get(currentNode.getParentId());
        //击中缓存
        if (parentNode != null) {
            parentNode.addChildNode(currentNode);//添加子节点
        }
        cacheMap.put(currentNode.getId(), currentNode);
    }

    /**
     * 递归遍历树型结构
     *
     * @param root     根节点
     * @param listener 读取每一个节点时的监听器
     * @return 扁平的所有节点list <strong>（深度优先）</strong>
     */
    public static List<TreeNode> readRootTree(TreeNode root, TreeReadListener listener) {
        return readTreeByDeep(root.getChildren(), new ArrayList<>(), listener);
    }

    /**
     * 递归遍历树型结构
     *
     * @param forest   森林
     * @param listener 读取每一个节点时的监听器
     * @return 扁平的所有节点list <strong>（深度优先）</strong>
     */
    public static List<TreeNode> readForest(List<TreeNode> forest, TreeReadListener listener) {
        return readTreeByDeep(forest, new ArrayList<>(), listener);
    }

    /**
     * 递归遍历树型结构<strong>（深度优先）</strong>
     *
     * @param forest     森林
     * @param resultList 结果集，不能为null
     * @return 结果集
     */
    private static List<TreeNode> readTreeByDeep(List<TreeNode> forest, List<TreeNode> resultList, TreeReadListener listener) {
        if (forest != null) {
            for (TreeNode node : forest) {
                resultList.add(node);
                //遍历读取时的回调
                if (listener != null) {
                    listener.onRead(node);
                }
                readTreeByDeep(node.getChildren(), resultList, listener);
            }
        }
        return resultList;
    }

}
