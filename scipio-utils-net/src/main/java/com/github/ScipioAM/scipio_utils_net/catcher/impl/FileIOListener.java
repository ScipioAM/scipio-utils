package com.github.ScipioAM.scipio_utils_net.catcher.impl;

import com.github.ScipioAM.scipio_utils_net.catcher.IOListener;
import com.github.ScipioAM.scipio_utils_net.catcher.bean.CatchResult;
import com.github.ScipioAM.scipio_utils_net.catcher.bean.WebInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 文件件IO操作的监听器实现 <br/>
 * (以TXT文件为主)
 * @author Alan Scipio
 * @since 2021/6/9
 */
public class FileIOListener implements IOListener {

    private String filePath; //文件路径
    private String fileName; //文件名
    private String fileSuffix = ".txt"; //文件后缀

    private boolean isAppend = false; //是否为追加，为false则不是追加而是覆盖

    public FileIOListener() {}

    public FileIOListener(String filePath, String fileName, String fileSuffix) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSuffix = fileSuffix;
    }

    /**
     * IO操作的回调
     * @param webInfo 目标网页信息，前提是其catchResult.resultStrList有值
     * @param params 附加参数
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onProcess(WebInfo webInfo, Object... params) {
        List<String> contentList = getFileContent(webInfo);
        File parentDir = new File(filePath);
        File file = new File(filePath + File.separatorChar + fileName + fileSuffix);
        //创建父目录(如果不存在)
        if(!parentDir.exists()) {
            parentDir.mkdirs();
            System.out.println("parent directories created: " + parentDir.getPath());
        }
        //创建文件(如果不存在)
        if(!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("file created: " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        //开始将内容写入文件
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,isAppend))) {
            System.out.println("start write data into file["+file.getPath()+"], isAppend if file existed:"+isAppend);
            for(String line : contentList) {
                line += "\n";
                writer.write(line);
                writer.flush();
            }
            System.out.println("file have written");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }//end of onProcess()

    /**
     * 获取结果集 <br/>
     * 注：可以重写该方法以获取其他结果集
     * @param webInfo 网页信息（包括抓取结果）
     * @return 要写入文件的内容
     */
    protected List<String> getFileContent(WebInfo webInfo) {
        //结果集检查
        CatchResult catchResult = webInfo.getCatchResult();
        if(catchResult==null){
            throw new IllegalArgumentException("catchResult in webInfo is null while do FileIOListener.onProcess()");
        }
        List<String> resultStrList = catchResult.getResultStrList();
        if(resultStrList==null || resultStrList.size()<=0) {
            throw new IllegalArgumentException("resultStrList in catchResult is null while do FileIOListener.onProcess()");
        }
        return resultStrList;
    }

    //=====================================================================================

    public String getFilePath() {
        return filePath;
    }

    public FileIOListener setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileIOListener setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public FileIOListener setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
        return this;
    }

    public boolean isAppend() {
        return isAppend;
    }

    public FileIOListener setAppend(boolean append) {
        isAppend = append;
        return this;
    }

}
