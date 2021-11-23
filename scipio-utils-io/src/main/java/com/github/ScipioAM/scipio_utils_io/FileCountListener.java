package com.github.ScipioAM.scipio_utils_io;

import java.io.File;
import java.util.List;

/**
 * @since 1.0.9
 * @author Alan Scipio
 * @date 2021/11/23
 */
@FunctionalInterface
public interface FileCountListener {

    void onCounting(File file, boolean isCountDir, List<File> fileList);

    FileCountListener PRINTER = (file,isCountDir,fileList) -> System.out.println(file.getPath());

}
