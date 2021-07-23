package com.github.ScipioAM.scipio_utils_io.parser;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.*;

/**
 * Class: XmlParser
 * Description:
 *  xml解析
 *  需要依赖：XStream,dom4j
 * Author: Alan Min
 * Createtime: 2018/5/16
 */
public class XmlParser {

    private XStream xstream;

    //日期转换器
    private static final DateConverter DATE_CONVERTER =
            new DateConverter("yyyy-MM-dd HH:mm:ss", null, TimeZone.getTimeZone("GMT+8"));

    //设置是否扩展CDATA
    private boolean isSetCDATA=false;

    public XmlParser()
    {
        extendCDATA();
    }

    public XmlParser(boolean isSetCDATA) {
        this.isSetCDATA=isSetCDATA;
        extendCDATA();
    }

    /**
     * 扩展XStream使其支持CDATA
     */
    private void extendCDATA()
    {
        xstream = new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                    }
                    protected void writeText(QuickWriter writer, String text) {
                        if (isSetCDATA) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                        } else {
                            writer.write(text);
                        }//end of else
                    }//end of writeText()
                };//end of return
            }// end of createWriter()
        });
    }

    //----------------------------------------------------------------------

    /**
     * xml转java对象
     * @param xml 要转换的xml字符串
     * @param clazz 目标java对象
     */
    @SuppressWarnings("unchecked")
    public <T> T xmlToObject(String xml, Class<T> clazz) {
        //XStream1.5之后移除了该方法
        XStream.setupDefaultSecurity(xstream);
        //XStream的安全设置之二
        xstream.allowTypeHierarchy(clazz);
        // 注册日期转换器
        xstream.registerConverter(DATE_CONVERTER);
        xstream.processAnnotations(clazz);
        //开始转换
        return (T) xstream.fromXML(xml);
    }

    /**
     * 从xml文件读取并转java对象
     * @param filePath xml文件的全路径
     * @param clazz 目标java对象
     */
    @SuppressWarnings("unchecked")
    public <T> T xmlFileToObject(String filePath, Class<T> clazz) {
        //XStream1.5之后移除了该方法
//        XStream.setupDefaultSecurity(xstream);
        //XStream的安全设置之二
        xstream.allowTypeHierarchy(clazz);
        // 注册日期转换器
        xstream.registerConverter(DATE_CONVERTER);
        xstream.processAnnotations(clazz);
        //开始转换
        return (T) xstream.fromXML(new File(filePath));
    }

    /**
     * java对象转xml
     * @param obj 要转换的java对象
     */
    public String objectToXml(Object obj) {
        //XStream1.5之后移除了该方法
        XStream.setupDefaultSecurity(xstream);
        // 注册日期转换器
        xstream.registerConverter(DATE_CONVERTER);
        xstream.processAnnotations(obj.getClass());
        return xstream.toXML(obj);
    }

    /**
     * java对象转xml，同时设置别名（根标签名称）
     * @param obj 要转换的java对象
     * @param alias 别名（根标签名称）
     * @param clazz 要转换java对象的类型
     */
    public String objectToXml(Object obj,String alias,Class clazz) {
        XStream.setupDefaultSecurity(xstream);//XStream1.5之后移除了该方法
        xstream.registerConverter(DATE_CONVERTER);
        xstream.processAnnotations(obj.getClass());
        setAlias(alias,clazz);
        return xstream.toXML(obj);
    }

    /**
     * java对象转xml并输出到文件
     * @param obj 要转换的java对象
     * @param filePath 输出文件的全路径
     */
    public void objectToXmlFile(Object obj, String filePath) throws FileNotFoundException {
        //XStream1.5之后移除了该方法
        XStream.setupDefaultSecurity(xstream);
        xstream.registerConverter(DATE_CONVERTER);
        xstream.processAnnotations(obj.getClass());
        // create target file
        FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
        // output
        xstream.toXML(obj, fileOutputStream);
    }

    /**
     * java对象转xml并输出到文件
     * @param obj 要转换的java对象
     * @param filePath 输出文件的全路径
     * @param alias 别名（根标签名称）
     * @param clazz 要转换的java对象的类型
     */
    public void objectToXmlFile(Object obj, String filePath,String alias,Class clazz) throws FileNotFoundException {
        XStream.setupDefaultSecurity(xstream);//XStream1.5之后移除了该方法
        xstream.registerConverter(DATE_CONVERTER);
        xstream.processAnnotations(obj.getClass());
        FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
        setAlias(alias,clazz);
        xstream.toXML(obj, fileOutputStream);
    }

    /**
     * 设置根节点别名，否则默认类全名
     * @param alias 根节点别名
     * @param clazz 要转换的类
     */
    public XmlParser setAlias(String alias, Class clazz){
        xstream.alias(alias,clazz);
        return this;
    }

    public XStream getXStream() {
        return xstream;
    }

    public XmlParser setXStream(XStream xstream) {
        this.xstream = xstream;
        return this;
    }

    public boolean isSetCDATA() {
        return isSetCDATA;
    }

    public XmlParser setCDATA(boolean setCDATA) {
        isSetCDATA = setCDATA;
        return this;
    }

    //----------------------------------------------------------------------

    /**
     * 从字符串里解析xml数据为键值对(利用dom4j框架)
     */
    public Document parseXml2Doc(String xml) throws IOException, DocumentException
    {
        return doParseXml(xml,XML_PARAM_STRING);
    }

    /**
     * 输入流里解析xml数据为键值对(利用dom4j框架)
     */
    public Document parseXmlStream2Doc(InputStream in) throws IOException, DocumentException
    {
        return doParseXml(in,XML_PARAM_STREAM);
    }

    /**
     * 从文件里解析xml数据为键值对(利用dom4j框架)
     */
    public Document parseXmlFile2Doc(String filePath) throws IOException, DocumentException
    {
        return doParseXml(filePath,XML_PARAM_FILE_PATH);
    }

    /**
     * 解析xml文档并以键值对的形式存放
     * @param document dom4j的xml文档对象
     * @return 解析结果集：key为标签名，value为找到的该名称的标签列表
     */
    public Map<String,List<Element>> parseXml(Document document)
    {
        Map<String,List<Element>> resultMap=new HashMap<>();
        //存入根标签
        Element rootElement=document.getRootElement();
        List<Element> rootList=new ArrayList<>();
        rootList.add(rootElement);
        resultMap.put(rootElement.getName(),rootList);
        //存入其他所有标签
        findAllElements(document.getRootElement(),resultMap);
        return resultMap;
    }

    /**
     * 解析xml文档并以键值对的形式存放
     * @param filePath xml文件的全路径
     */
    public Map<String,List<Element>> parseXml(String filePath) throws IOException, DocumentException
    {
        Document document=doParseXml(filePath,XML_PARAM_FILE_PATH);
        return parseXml(document);
    }

    /**
     * 解析xml文档并以键值对的形式存放
     * @param is 以输入流形式存在的xml文档
     */
    public Map<String,List<Element>> parseXml(InputStream is) throws IOException, DocumentException
    {
        Document document=doParseXml(is,XML_PARAM_STREAM);
        return parseXml(document);
    }

    /**
     * 获取xml里指定的标签对象
     * @param is 从输入流里读取xml数据
     * @param name 指定的xml标签名称
     */
    public List<Element> getElementsByName(InputStream is,String name) throws IOException, DocumentException
    {
        Document document=doParseXml(is,XML_PARAM_STREAM);
        return getElementsByName(document,name);
    }

    /**
     * 获取xml里指定的标签对象
     * @param xml 从字符串里读取xml数据
     * @param name 指定的xml标签名称
     */
    public List<Element> getElementsByName(String xml,String name) throws IOException, DocumentException
    {
        Document document=doParseXml(xml,XML_PARAM_STRING);
        return getElementsByName(document,name);
    }

    /**
     * 获取xml里指定的标签对象
     * @param document 从Document对象里读取xml数据
     * @param name 指定的xml标签名称
     */
    public List<Element> getElementsByName(Document document,String name)
    {
        List<Element> elementList=new ArrayList<>();
        Element root=document.getRootElement();
        if( root.getName().equals(name) )
        {
            elementList.add(root);
        }
        else
        {
            findAllElements(root,elementList,name);
        }
        return elementList;
    }

    /**
     * 输出Document对象到本地文件
     * @param document xml文档对象
     * @param filePath 要输出的文件路径全名
     * @param isPrettyPrint 是否输出为美化的格式（为false则输出紧凑格式）
     */
    public void writeDocument(Document document,String filePath,boolean isPrettyPrint) throws IOException
    {
        File file=new File(filePath);
        if( !file.exists() || file.isDirectory() )
            throw new IOException("File not found or is a directory path,which path is {"+filePath+"}");

        OutputFormat format;
        if(isPrettyPrint)
            format=OutputFormat.createPrettyPrint();//美化格式
        else
            format=OutputFormat.createCompactFormat();//紧凑格式

        XMLWriter writer=new XMLWriter(new FileOutputStream(filePath),format);
        writer.write(document);
        writer.close();
    }

    //----------------------------------------------------------------------
    private static final int XML_PARAM_STRING=0;//要解析的数据是string
    private static final int XML_PARAM_FILE_PATH=1;//要解析的数据是文件
    private static final int XML_PARAM_STREAM=2;//要解析的数据是输入流

    /**
     * 利用dom4j解析xml
     * @param xml 要解析的xml数据
     * @param xmlParamMode xml数据的模式
     */
    private Document doParseXml(Object xml,int xmlParamMode) throws DocumentException, IOException
    {
        Document document;
        SAXReader reader;
        if(xmlParamMode==XML_PARAM_STRING)
        {
            document= DocumentHelper.parseText((String) xml);
        }
        else if(xmlParamMode==XML_PARAM_STREAM)
        {
            reader=new SAXReader();
            document=reader.read((InputStream) xml);
        }
        else
        {
            reader=new SAXReader();
            document=reader.read(new File((String) xml));
        }

        if( xmlParamMode==XML_PARAM_STREAM )//读完xml，反手关闭输入流
        {
            InputStream in=(InputStream)xml;
            in.close();
            in=null;
        }
        return document;
    }

    /**
     * 递归遍历，查找所有（指定的）元素
     * @param parentElement 父元素
     * @param resultList 查找结果集
     * @param name 要查找元素的名称(为null则获取父元素下的所有子元素)
     */
    private void findAllElements(Element parentElement ,List<Element> resultList,String name)
    {
        if(resultList==null)
        {
            resultList=new ArrayList<>();
        }

        List<Element> childList=parentElement.elements();
        for( Element e : childList )
        {
            if( name!=null && !"".equals(name))//如果需要查找指定标签
            {
                if(e.getName().equals(name))
                {
                    resultList.add(e);
                }
            }
            else//不需要查找指定标签，查找全部标签
            {
                resultList.add(e);
            }
            findAllElements(e,resultList,name);
        }//end of for
    }//end of findAllElements()

    private void findAllElements(Element parentElement,Map<String,List<Element>> resultMap)
    {
        if(resultMap==null)
        {
            resultMap=new HashMap<>();
        }

        List<Element> childList=parentElement.elements();
        for( Element e : childList )
        {
            String key=e.getName();//获取标签名称
            List<Element> valueList=resultMap.get(key);//获取该标签的列表
            if(valueList==null)//第一次找到该标签
            {
                valueList=new ArrayList<>();
                valueList.add(e);
                resultMap.put(key,valueList);//加入到结果集里
            }
            else//不是第一次找到该标签
            {
                valueList.add(e);
            }
            findAllElements(e,resultMap);//递归调用查找
        }
    }

}
