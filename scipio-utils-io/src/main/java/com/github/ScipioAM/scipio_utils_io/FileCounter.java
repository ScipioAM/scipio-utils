package com.github.ScipioAM.scipio_utils_io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @since 1.0.9
 * @author Alan Scipio
 * @date 2021/11/23
 */
public class FileCounter {

    private FileCountListener fileCountListener;

    //================================ ↓↓↓↓↓↓ 文件统计 ↓↓↓↓↓↓ ================================

    /**
     * 统计所有文件数
     * @param parentFile 父目录/根目录
     * @param isCountDir 目录是否也统计进去
     * @param fileList 所有文件（不包括目录）
     * @return 统计的文件数
     */
    public int count(File parentFile, boolean isCountDir, List<File> fileList) throws FileNotFoundException {
        if(!parentFile.exists()) {
            throw new FileNotFoundException("non exists path: " + parentFile.getAbsolutePath());
        }
        if(fileCountListener != null) {
            fileCountListener.onCounting(parentFile,isCountDir,fileList);
        }

        int countNum = 0;
        if(parentFile.isDirectory()) { //是目录
            if(isCountDir) { //统计目录（包括最上层的根目录本身也被计数）
                countNum++;
            }
            File[] subFiles = parentFile.listFiles();
            if(subFiles == null) { //目录下没有文件
                return 0;
            }
            for(File subFile : subFiles) { //目录下有文件或子文件夹
                int subCountNum = count(subFile, isCountDir, fileList);
                countNum += subCountNum;
            }
        }
        else { //是文件，计数加1
            if(fileList != null) {
                fileList.add(parentFile);
            }
            return 1;
        }
        return countNum;
    }

    /**
     * 统计所有文件数（不统计目录本身的个数）
     * @param rootFile 根目录
     * @param fileList 所有文件（不包括目录）
     * @return 统计的文件数
     */
    public int count(File rootFile, List<File> fileList) throws FileNotFoundException {
        return count(rootFile,false,fileList);
    }

    public int count(File rootFile) throws FileNotFoundException {
        return count(rootFile,false,null);
    }

    //================================ ↓↓↓↓↓↓ 文件遍历 ↓↓↓↓↓↓ ================================

    public FileCountStream stream(File root) throws FileNotFoundException {
        if(root == null) {
            throw new NullPointerException("root file is null");
        }
        else if(!root.exists()) {
            throw new FileNotFoundException("non exists path: " + root.getAbsolutePath());
        }
        return new FileCountStream(root);
    }

    //================================ ↓↓↓↓↓↓ 其他 ↓↓↓↓↓↓ ================================

    public void setFileCountListener(FileCountListener fileCountListener) {
        this.fileCountListener = fileCountListener;
    }
}
