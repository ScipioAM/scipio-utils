package com.github.ScipioAM.scipio_utils_io;

import com.github.ScipioAM.scipio_utils_common.validation.annotation.NotNull;

import java.io.File;
import java.util.Stack;

/**
 * @since 1.0.9
 * @author Alan Scipio
 * @date 2021/11/23
 */
public class FileCountStream {

    private final File rootFile;

    private final Stack<File> dirStack = new Stack<>();

    private final Stack<File> currentFileStack = new Stack<>();

    public FileCountStream(@NotNull File rootFile) {
        this.rootFile = rootFile;
        File[] subFiles = rootFile.listFiles();
        if(subFiles != null) {
            setStack(subFiles);
        }
    }//end of Constructor

    //================================ ↓↓↓↓↓↓ 调用的方法 ↓↓↓↓↓↓ ================================

    public boolean hasNext() {
        return (currentFileStack.size() > 0 || dirStack.size() > 0);
    }

    public File next() {
        if(currentFileStack.size() > 0) {
            return currentFileStack.pop();
        }
        else if(dirStack.size() > 0) {
            File dir = dirStack.pop();
            File[] subFiles = dir.listFiles();
            if(subFiles != null) {
                setStack(subFiles);
                return next();
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public File getRootFile() {
        return rootFile;
    }

    //================================ ↓↓↓↓↓↓ 内部私有方法 ↓↓↓↓↓↓ ================================

    private void setStack(File[] subFiles) {
        for(File subFile : subFiles) {
            if(subFile.isDirectory()) {
                dirStack.push(subFile);
            }
            else {
                currentFileStack.push(subFile);
            }
        }
    }

}
