package com.github.ScipioAM.scipio_utils_io;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Class: FileUtil
 * Description: 文件操作工具
 * Author: Alan Min
 * Create Time: 2018/5/3
 */
public class FileUtil {

    private FileUtil(){}

    /**
     * 拷贝文件
     * @param sourceFile: 源文件
     * @param targetFile: 目标文件，不存在就创建
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        if(!sourceFile.exists()) {
            throw new FileNotFoundException("source file is not found");
        }
        checkFileOrCreate(targetFile);
        try (FileInputStream fis = new FileInputStream(sourceFile); FileOutputStream fos = new FileOutputStream(targetFile)){
            FileChannel in = fis.getChannel();//得到对应的文件通道
            FileChannel out = fos.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入
        }
    }

    /**
     * 拷贝文件
     * @return 目标文件
     */
    public static File copyFile(String sourcePath, String targetPath) throws IOException {
        File sourceFile=new File(sourcePath);
        File targetFile=new File(targetPath);
        copyFile(sourceFile,targetFile);
        return targetFile;
    }

    /**
     * 删除指定文件夹及其所有子文件夹
     * @param dir 要删除的文件对象
     * @return 是否成功
     */
    public static boolean delAllFiles(File dir) {
        System.out.println(dir.getParent());
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            if(children==null)
                return true;
            for (String child:children)//递归删除子目录下的所有文件
            {
                boolean success = delAllFiles(new File(dir, child));
                if (!success) {
                    return false;
                }
            }//end of for
        }//end of outside if
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 删除指定文件夹下的所有文件(本身不删)
     * @param dirPath 文件全路径（包括文件自己的名称）
     * @return 删除是否成功
     */
    public static boolean delSubFiles(String dirPath) {
        boolean isSuccess = true;
        File dir = new File(dirPath);
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children == null) {
                return true;
            }
            for (File f : children) {
                if (!f.delete()) {
                    isSuccess = false;
                    break;
                }
            }//end of for
        }
        return isSuccess;
    }

    /**
     * 检查文件夹下是否有文件（不包括子文件夹里）
     * @param dirPath 文件夹路径
     * @return 子文件个数
     */
    public static int haveFile(String dirPath)
    {
        File dir = new File(dirPath);
        if (!dir.exists())
            return 0;
        if (!dir.isDirectory())
            return 0;
        String[] childNames = dir.list();
        if (childNames == null)
            return 0;
        else
            return childNames.length;
    }

    /**
     * 将字节数据写入一个文件
     * @param file 要写入的文件对象
     * @param data 要写入的数据
     * @throws IOException 写入失败
     */
    public static void writeBytes(File file, byte[] data) throws IOException {
        try (OutputStream fos = new FileOutputStream(file); OutputStream os = new BufferedOutputStream(fos)) {
            os.write(data);
        }
    }//end of writeBytes()

    /**
     * 将字符串内容写入一份文件里
     * @param filePath: 文件路径（不包括文件名）
     * @param fileName: 文件名（包括后缀）
     * @param content: 写入内容
     * @throws IOException 创建文件或目录失败，写入失败
     */
    public static void writeTxtFile( String filePath, String fileName, String content) throws IOException{
        File parentDir = new File(filePath);
        File file = new File(filePath+File.separatorChar+fileName);
        boolean isNeedAppend = false;//是否为追加

        //父目录不存在
        if(!parentDir.exists()) {
            if(!parentDir.mkdirs()){ //创建父目录
                throw new IOException("file's parent directory created failed!");
            }
            //创建文件（父目录不存在，则文件肯定不存在）
            if(!file.createNewFile()) {
                throw new IOException("Error: file created failed!");
            }
        }
        //父目录存在
        else {
            //文件不存在
            if(!file.exists()) {
                //创建文件
                if (!file.createNewFile()) {
                    throw new RuntimeException("file created failed!");
                }
            }
            //文件存在
            else {
                isNeedAppend = true;
            }
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,isNeedAppend))) {
            writer.write(content);
            writer.flush();
        }
    }

    /**
     * 从文件读取字符串
     * @param filePath 文件全路径
     * @param bufferSize 一次读取的缓冲池大小（单位是一个字符）
     * @param charset 字符集
     */
    public static String readStrFromFile(String filePath, int bufferSize, Charset charset) throws IOException{
        File file = new File(filePath);
        if(!file.exists()) {
            throw new FileNotFoundException("file can not be found! path: "+filePath);
        }
        else if(file.isDirectory()) {
            throw new IllegalArgumentException("file path can not be a directory path!");
        }

        StringBuilder sb = new StringBuilder();
        try (FileInputStream is = new FileInputStream(file); InputStreamReader reader = new InputStreamReader(is, charset)){
            int count;
            char[] buffer = new char[bufferSize];//每次读取的缓冲区，其大小决定了读取速度
            while ((count = reader.read(buffer)) != -1) {  //读取
                sb.append(new String(buffer,0,count));
            }
        }
        return sb.toString();
    }

    /**
     * 从文件读取字符串 - 默认缓冲池大小
     */
    public static String readStrFromFile(String filePath, String charset) throws IOException{
        return readStrFromFile(filePath,128,Charset.forName(charset));
    }

    /**
     * 从文件读取字符串 - 默认字符集，默认缓冲池大小
     */
    public static String readStrFromFile(String filePath) throws IOException{
        return readStrFromFile(filePath,128,Charset.defaultCharset());
    }

    /**
     * 检查给定的文件对象是否为一个文件路径，如果是但不存在的话就创建
     * @param file 给定的文件全路径
     * @throws IllegalArgumentException 给定的文件对象为文件夹时，抛出此异常
     * @throws IOException 创建失败
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void checkFileOrCreate(File file) throws IllegalArgumentException,IOException {
        //检查这个文件是否存在
        if (!file.exists()) {
            if(file.isDirectory()) {
                throw new IllegalArgumentException("local file path can not be a directory path!");
            }
            try {
                //确保父目录存在
                File parentFile = file.getParentFile();
                if(!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                //开始创建
                file.createNewFile();
            }catch (IOException e){
                throw new IOException("create file failed! path is: "+file.getPath());
            }
        }
    }

    /**
     * 将给定的输入流里的数据，输出到本地文件
     * @param filePath 本地文件地址
     * @param is 输出流
     * @throws IOException 读取或写入失败，或关闭输出流失败
     */
    public static void out2LocalFile(String filePath, InputStream is, int bufferSize) throws IOException{
        if(bufferSize<=0) {
            throw new IllegalArgumentException("buffer size must greater then 0");
        }
        if(is==null) {
            throw new IllegalArgumentException("InputStream is null");
        }
        //检查文件
        File localFile = new File(filePath);//获取本地文件对象
        checkFileOrCreate(localFile);
        //开始输出
        try (FileOutputStream os = new FileOutputStream(localFile)) {
            int count;
            byte[] buffer = new byte[bufferSize];//每次读取的缓冲区，其大小决定了读取速度
            while ((count = is.read(buffer)) != -1) {  //读取
                os.write(buffer, 0, count);//把读出的每一块，向客户端输出
                os.flush();
            }
        }finally {
            is.close();
        }
    }

    /**
     * 将给定的输入流里的数据，输出到本地文件 (默认1024大小的缓冲区)
     */
    public static void out2LocalFile(String filePath, InputStream is) throws IOException{
        out2LocalFile(filePath,is,1024);
    }

    /**
     * 将给定的字节数据，输出到本地文件
     * @param filePath 本地文件地址
     * @param data 要输出的字节数据
     * @throws IOException 读取或写入失败，或关闭输出流失败
     */
    public static void out2LocalFile(String filePath, byte[] data, int bufferSize) throws IOException{
        if(bufferSize<=0) {
            throw new IllegalArgumentException("buffer size must greater then 0");
        }
        //检查文件
        File localFile = new File(filePath);//获取本地文件对象
        checkFileOrCreate(localFile);
        //开始输出
        try (ByteArrayInputStream is = new ByteArrayInputStream(data);FileOutputStream os = new FileOutputStream(localFile)) {
            int count;
            byte[] buffer = new byte[bufferSize];//每次读取的缓冲区，其大小决定了读取速度
            while ((count = is.read(buffer)) != -1) {  //读取
                os.write(buffer, 0, count);//把读出的每一块，向客户端输出
                os.flush();
            }
        }
    }
    /**
     * 将给定的字节数据，输出到本地文件(默认1024大小的缓冲区)
     */
    public static void out2LocalFile(String filePath, byte[] data) throws IOException{
        out2LocalFile(filePath,data,1024);
    }


}
