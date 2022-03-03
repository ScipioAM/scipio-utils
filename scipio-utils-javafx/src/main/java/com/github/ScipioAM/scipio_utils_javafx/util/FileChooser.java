package com.github.ScipioAM.scipio_utils_javafx.util;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

import java.io.File;

/**
 * 文件选择器
 *
 * @author Alan Scipio
 * @since 1.0.0 _ 2020/7/18
 */
public class FileChooser {

    /**
     * 显示选择文件的窗口
     *
     * @param window  程序窗体对象
     * @param title   标题
     * @param filters 后缀过滤器
     * @return 被选中的文件对象
     */
    public static File chooseFile(Window window, String title, ExtensionFilter... filters) {
        javafx.stage.FileChooser chooser = new javafx.stage.FileChooser();
        chooser.setTitle(title);
        if (filters != null) {
            chooser.getExtensionFilters().addAll(filters);
        }
        return chooser.showOpenDialog(window);
    }

    /**
     * 显示选择文件夹的窗口
     *
     * @param window 程序窗体对象
     * @param title  标题
     * @return 被选中的文件夹对象
     */
    public static File chooseDir(Window window, String title) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        return chooser.showDialog(window);
    }

}
