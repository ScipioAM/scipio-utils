package com.github.ScipioAM.scipio_utils_common.reflect;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包扫描器
 * @author Alan Scipio
 * @since 2021/4/13
 */
public class PackageScanner {

    private PackageScanHandler handler;
    private final Map<String,Class<?>> classMap = new HashMap<>();

    //===============================================================================================

    /**
     * 扫描多个包
     * @param packageNameArr 多个包的包名
     * @return 扫描到的类的集合
     */
    public Map<String,Class<?>> scanPackages(String... packageNameArr) throws IOException {
        for(String packageName : packageNameArr) {
            scanPackages(packageName);
        }
        return classMap;
    }

    /**
     * 扫描包
     * @param clazz 目标对象
     * @return 扫描到的类的集合
     */
    public Map<String,Class<?>> scanPackages(Class<?> clazz) throws IOException {
        String path = clazz.getPackage().getName();
        scanPackages(path);
        return classMap;
    }

    /**
     * 扫描多个包
     * @param classArr 多个包
     * @return 扫描到的类的集合
     */
    public Map<String,Class<?>> scanPackages(Class<?>[] classArr) throws IOException {
        for(Class<?> clazz : classArr) {
            scanPackages(clazz);
        }
        return classMap;
    }

    /**
     * 【核心入口】扫描包
     * @param rootPackageName 根包的包名
     * @return 扫描到的类的集合
     * @throws IOException 获取包里的文件失败
     */
    public Map<String,Class<?>> scanPackages(String rootPackageName) throws IOException {
        //将传过来的包名里的“.”转换为“/”，供下面的类加载器调用找到类加载器的资源
        String packagePath = rootPackageName.replace(".", "/");
        //获取当前线程的类加载器，以供后续使用
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //查找文件，并遍历扫描
        Enumeration<URL> resources = classLoader.getResources(packagePath);
        try {
            while(resources.hasMoreElements()) {
                URL url = resources.nextElement();
                //对jar包进行包扫描
                if(url.getProtocol().equals("jar")){
                    scanJarPackage(url,classMap);
                }
                //对普通的类进行包扫描
                else {
                    File currentPackage = new File(url.toURI());
                    //如果这个文件不存在，就继续查找
                    if (!currentPackage.exists()) {
                        continue;
                    }
                    scanCommonPackage(currentPackage,rootPackageName,classMap);
                }
            }//end of while
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return classMap;
    }//end of scanPackages()

    /**
     * 递归扫描普通包
     * @param curPackage 当前包（也就是文件夹）
     * @param packageName 当前包名
     * @param classMap 扫描到的类的集合
     */
    private void scanCommonPackage(File curPackage, String packageName, Map<String,Class<?>> classMap) throws IOException {
        //如果不是目录就结束方法的调用
        if(!curPackage.isDirectory()) {
            return;
        }
        File[] files = curPackage.listFiles();
        if(files==null) {
            throw new IOException("package["+packageName+"] dont have child class or packages!");
        }
        for(File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String fileName = file.getName().replace(".class", "");
                //去掉“.class”后就是文件名，路径名加文件名就是类名
                String className = packageName + "." + fileName;
                try {
                    //根据类名称得到类类型
                    Class<?> clazz = Class.forName(className);
                    //记录下来
                    classMap.put(className,clazz);
                    //对扫描到的类进行处理
                    if(handler!=null) {
                        handler.handleClass(clazz);
                    }
                    System.out.println("Class["+className+"] has been scanned");
                }catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if(file.isDirectory()) {
                scanCommonPackage(file,packageName + "." + file.getName(),classMap);
            }
        }//end of for
    }//end of scanCommonPackage()

    /**
     * 递归遍历jar里的包
     * @param url jar包的url
     * @param classMap 扫描到的类的集合
     */
    private void scanJarPackage(URL url, Map<String,Class<?>> classMap) throws IOException {
        //获得jar包对象
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        //进入jar包进行扫描
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while(jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarName = jarEntry.getName();
            //如果它是一个目录或者不是“.class”文件，就跳过
            if (jarEntry.isDirectory() || !jarName.endsWith(".class")) {
                continue;
            }
            String className = jarName.replace(".class", "").replaceAll("/", ".");
            try {
                Class<?> clazz = Class.forName(className);
                //如果这个类是注解或者枚举或者接口或者八大基本类型就跳过
                if (clazz.isAnnotation()
                        || clazz.isEnum()
                        || clazz.isInterface()
                        || clazz.isPrimitive()) {
                    continue;
                }
                //记录下来
                classMap.put(className,clazz);
                //对扫描到的类进行处理
                if(handler!=null) {
                    handler.handleClass(clazz);
                }
                System.out.println("Class["+className+"] which from jar, has been scanned");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }//end of while
    }//end of scanJarPackage()

    //===============================================================================================

    public PackageScanHandler getHandler() {
        return handler;
    }

    public void setHandler(PackageScanHandler handler) {
        this.handler = handler;
    }

    public Map<String, Class<?>> getClassMap() {
        return classMap;
    }
}
