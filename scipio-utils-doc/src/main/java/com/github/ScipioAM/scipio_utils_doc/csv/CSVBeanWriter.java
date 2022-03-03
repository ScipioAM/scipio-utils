package com.github.ScipioAM.scipio_utils_doc.csv;

import com.github.ScipioAM.scipio_utils_common.AssertUtil;
import com.github.ScipioAM.scipio_utils_doc.csv.bean.CSVCell;
import com.github.ScipioAM.scipio_utils_doc.csv.bean.CSVMeta;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * CSV写入
 * @author Alan Scipio
 * @since 2021/12/30
 */
public class CSVBeanWriter {

    /** 分隔符 */
    private String DELIMITER = ",";

    /** 字符集 */
    private String charset = "utf-8";

    /**
     * CSV写入
     * @param beanClass JavaBean的Class类型
     * @param beanList JavaBean数据
     * @param out 输出流
     * @param <T> JavaBean的类型
     * @throws IOException 写入失败
     * @throws IllegalAccessException 读取注解信息，反射获得字段值失败
     */
    public <T> void write(Class<T> beanClass, List<T> beanList, OutputStream out) throws IOException, IllegalAccessException {
        AssertUtil.notNull(beanClass,"beanClass can not be null");
        AssertUtil.collectionNotEmpty(beanList,"beanList can not be null or empty");
        //确定元信息
        List<CSVMeta> metaList = new ArrayList<>();
        Field[] fields = beanClass.getDeclaredFields();
        for(Field field : fields) {
            if(field.isAnnotationPresent(CSVCell.class)) {
                CSVCell annotation = field.getDeclaredAnnotation(CSVCell.class);
                CSVMeta meta = new CSVMeta(annotation.sort(),annotation.title(),field);
                metaList.add(meta);
            }
        }
        //根据sort字段排序
        metaList.sort(Comparator.comparingInt(CSVMeta::getSort));

        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, charset))) {
            //写入bom（为了让Excel能正常显示utf-8编码下的中文）
            writer.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
            //写入标题
            String titleLine = buildLine(metaList,null,true);
            writer.write(titleLine);
            //写入内容
            for(T bean : beanList) {
                //构建内容行
                String contentLine = buildLine(metaList,bean,false);
                writer.write(contentLine);
            }
            writer.flush();
        }
    }

    /**
     * CSV写入
     * @param beanClass JavaBean的Class类型
     * @param beanList JavaBean数据
     * @param <T> JavaBean的类型
     * @return 写入的内容（字节数组形式）
     * @throws IOException 写入失败
     * @throws IllegalAccessException 读取注解信息，反射获得字段值失败
     */
    public <T> byte[] write(Class<T> beanClass, List<T> beanList) throws IOException, IllegalAccessException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        write(beanClass,beanList,out);
        return out.toByteArray();
    }

    /**
     * CSV写入到文件
     * @param beanClass JavaBean的Class类型
     * @param beanList JavaBean数据
     * @param <T> JavaBean的类型
     * @throws IOException 写入失败
     * @throws IllegalAccessException 读取注解信息，反射获得字段值失败
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public <T> void write2File(Class<T> beanClass, List<T> beanList, File file) throws IOException, IllegalAccessException {
        AssertUtil.notNull(file,"argument 'file' can not be null");
        //父目录检查
        File parentFile = file.getParentFile();
        if(!parentFile.exists()) {
            file.mkdirs();//不存在就创建齐父目录
        }
        //文件本身检查
        if(!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file);
        write(beanClass,beanList,out);
    }

    //==================================================================================================================================

    /**
     * 构建行
     * @param buildTitleLine 是否为构建标题行，true代表是
     */
    private String buildLine(List<CSVMeta> metaList, Object bean, boolean buildTitleLine) throws IllegalAccessException {
        StringBuilder line = new StringBuilder();
        for(CSVMeta meta : metaList) {
            line.append("\"")
                    .append(buildTitleLine ? meta.getTitle() : meta.getFieldValue(bean))
                    .append("\"")
                    .append(DELIMITER);
        }
        line.deleteCharAt(line.length() - 1);
        line.append("\n");
        return line.toString();
    }

    //==================================================================================================================================

    public String getDelimiter() {
        return DELIMITER;
    }

    public void setDelimiter(String delimiter) {
        this.DELIMITER = delimiter;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
