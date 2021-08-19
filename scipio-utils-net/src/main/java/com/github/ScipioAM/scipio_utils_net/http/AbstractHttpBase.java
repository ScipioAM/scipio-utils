package com.github.ScipioAM.scipio_utils_net.http;

import com.github.ScipioAM.scipio_utils_io.parser.StreamParser;
import com.github.ScipioAM.scipio_utils_net.http.common.RequestDataMode;
import com.github.ScipioAM.scipio_utils_net.http.common.RequestMethod;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;
import com.github.ScipioAM.scipio_utils_net.http.listener.DownloadListener;
import com.github.ScipioAM.scipio_utils_net.http.listener.ResponseListener;
import com.github.ScipioAM.scipio_utils_net.http.listener.UploadListener;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Class: AbstractHttpBase
 * Description:
 * Author: Alan Min
 * Create Date: 2019/9/3
 */
public abstract class AbstractHttpBase {

    protected String charset = "UTF-8";//编码字符集，默认UTF-8

    protected String userAgent = null;

    protected Map<String, String> reqHeaderParam = null;//自定义请求头参数

    protected Proxy proxy = null;//代理

    protected boolean isFollowRedirects = false;//是否关闭重定向以获取跳转后的真实地址,默认false

    protected Integer fileBufferSize;//上传文件时的缓冲区大小

    protected String downloadFilePath;//下载文件的全路径，如果不为空则代表需要下载

    protected ResponseListener responseListener;//响应监听器（响应时的回调）

    protected UploadListener uploadListener;//上传监听器

    protected DownloadListener downloadListener;//下载监听器

    //----------------------------------------------------------------------------------------------

    /**
     * 发起http请求并获取返回的数据(不包括输出文件)
     * @param urlPath url路径
     * @param requestMethod get请求还是post请求
     * @param requestDataMode 发起请求时传参的方式，默认(比如x=1&y=2...)
     * @param responseDataMode 封装响应数据的模式
     * @return 响应数据
     */
    protected abstract ResponseResult doRequest(String urlPath, RequestMethod requestMethod, RequestDataMode requestDataMode,
                                                ResponseDataMode responseDataMode, Object requestParam)
            throws IOException, IllegalArgumentException, KeyManagementException;

    /**
     * 发起http请求，输出文件流（可能还包括其他参数），获取返回的数据
     * @param urlPath 请求的url
     * @param requestParams 请求的字符串参数
     * @param outputFiles 输出的文件参数
     * @param responseDataMode 封装响应数据的模式
     * @return 响应数据
     */
    protected abstract ResponseResult doFileRequest(String urlPath, Map<String,String> requestParams, Object outputFiles , ResponseDataMode responseDataMode )
            throws IOException, KeyManagementException, IllegalArgumentException;

    /**
     * 检查上传的文件（单个）有问题就抛异常
     */
    protected void checkPostFile(File file) throws IllegalArgumentException{
        if(!file.exists()) {
            throw new IllegalArgumentException("Post file is null or not exists");
        }
    }

    /**
     * 检查上传的文件（多个）有问题就抛异常
     */
    protected void checkPostFile(HashMap<String,File> files) throws IllegalArgumentException{
        for( Map.Entry<String,File> entry : files.entrySet() ) {
            if( !entry.getValue().exists() || entry.getValue().isDirectory()) {
                throw new IllegalArgumentException("file not exists or is a directory which key is ["+entry.getKey()+"]");
            }
        }
    }

    /**
     * 下载文件
     * @param outputFilePath 要输出的文件（全路径）
     * @param in 连接里响应的输入流，文件数据在此流中
     * @param contentLength 响应的文件字节大小
     */
    protected void downloadFile(String outputFilePath, InputStream in, int contentLength) {
        IOException ioe = null;
        boolean isSuccess = false;
        try (BufferedInputStream bis = new BufferedInputStream(in);
             FileOutputStream fos = new FileOutputStream(outputFilePath)
        ) {
            int length;//每次读取的字节数
            double readLength = 0.0;//总共读取的字节数
            byte[] buffer = new byte[getFileBufferSize()];
            while( (length=bis.read(buffer))!=-1 )
            {
                fos.write(buffer,0,length);
                readLength += length;
                double downloadedPercent = readLength/contentLength;//计算下载进度
                if(downloadListener!=null) {
                    downloadListener.onDownloading(downloadedPercent);
                }
            }
            isSuccess = true;
        }catch (IOException e) {
            ioe = e;
        }
        //下载结束时的回调
        if(downloadListener!=null) {
            downloadListener.onFinished(isSuccess,ioe);
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 获取当前设置的编码字符集
     */
    public String getCharset() {
        return charset;
    }

    /**
     * 获取当前设置的userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * 获取自定义请求头参数
     */
    public Map<String, String> getReqHeaderParam() {
        return reqHeaderParam;
    }

    /**
     * 检查当前设置的followRedirects参数情况
     * 参数含义：是否关闭重定向以获取跳转后的真实地址
     */
    public boolean isFollowRedirects() {
        return isFollowRedirects;
    }

    /**
     * 清除上传和响应监听器
     */
    public void clearListeners() {
        responseListener = null;
        uploadListener = null;
    }

    /**
     * 获取当前设置的 上传文件缓冲区大小 如果为空则取默认值
     * @param fileLength 文件总长度，单位是byte
     */
    public int getFileBufferSize(long fileLength) {
        if( fileBufferSize==null || fileBufferSize==0 ) {
            setDefaultBufferSize(fileLength);
        }
        return fileBufferSize;
    }

    public int getFileBufferSize() {
        if( fileBufferSize==null || fileBufferSize==0 ) {
            setDefaultBufferSize();
        }
        return fileBufferSize;
    }

    /**
     * 根据文件大小调整缓冲区的大小
     * @param fileLength 文件总长度，单位是byte
     */
    public void setDefaultBufferSize(long fileLength) {
        if( fileLength<=(1024*1024) )
            fileBufferSize=1024;
        else if ( fileLength<=(1024*1024*10) )
            fileBufferSize=4096;
        else
            fileBufferSize=1024*1024;
    }

    public void setDefaultBufferSize() {
        fileBufferSize=1024;
    }

    /**
     * 获取下载文件的全路径
     */
    public String getDownloadFilePath() {
        return downloadFilePath;
    }

    /**
     * 重置各项配置
     */
    public void resetConfig() {
        userAgent = null;
        reqHeaderParam = null;
        proxy = null;
        isFollowRedirects = false;
        fileBufferSize = null;
        responseListener = null;
        uploadListener = null;
        downloadListener = null;
        downloadFilePath = null;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 设置http和https连接共通的部分
     * @param conn http或https连接对象
     * @param contentType 请求头的contentType
     * @param requestMethod get请求还是post请求
     */
    protected void setCommonConnectionData(URLConnection conn, String contentType, RequestMethod requestMethod) {
        //设置共通的头信息，以及输出请求参数
        conn.setConnectTimeout(1000 * 10);//设置连接超时的时间(毫秒)
        conn.setReadTimeout(1000 * 60);
        conn.setDoInput(true);//设置连接打开输入流
        if(requestMethod== RequestMethod.POST) {
            conn.setDoOutput(true);//设置连接打开输出
        }
        conn.setRequestProperty("Accept-Encoding", "gzip,deflate");//告诉服务器支持gzip压缩
        conn.setRequestProperty("Accept-Charset", charset);
        conn.setRequestProperty("Charset", charset);
        //设置contentType
        if(contentType!=null)
            conn.setRequestProperty("Content-Type", contentType);//可被后面的headerParam覆盖
        //设置user-agent，默认user-agent是Mac系统上66版的chrome浏览器
        if(userAgent!=null && (!userAgent.equals("")) )
            conn.setRequestProperty("User-Agent",userAgent);
        //设置自定义头部参数
        if(reqHeaderParam!=null) {
            for(Map.Entry<String,String> entry: reqHeaderParam.entrySet())
                conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 依据返回的状态码进行处理
     * @param conn 连接对象
     * @param responseCode 响应码
     * @param responseDataMode 获取响应数据的模式
     */
    protected ResponseResult handleResponse(URLConnection conn, int responseCode, ResponseDataMode responseDataMode) throws IOException {
        ResponseResult result = new ResponseResult();
        result.setResponseCode(responseCode);
        result.setContentEncoding(conn.getContentEncoding());
        result.setContentLength(conn.getContentLength());
        result.setHeaders(conn.getHeaderFields());
        result.setConnObj(conn);

        if(responseCode>=200 && responseCode<300) {
            handleSuccess(result,responseCode,conn, responseDataMode);
        }
        else {
            //失败后的响应回调
            if(responseListener!=null)
                responseListener.onFailure(responseCode,conn);
        }
        return result;
    }

    /**
     * 响应成功时的处理（响应码:200）
     * @param responseCode 响应码
     * @param conn 连接对象
     * @param responseDataMode 获取响应数据的模式
     */
    private void handleSuccess(ResponseResult result, int responseCode, URLConnection conn, ResponseDataMode responseDataMode)
            throws IOException
    {
        //成功后的响应回调
        if(responseListener!=null)
            responseListener.onSuccess(responseCode,conn);

        InputStream in = conn.getInputStream();
        if(downloadFilePath!=null && !"".equals(downloadFilePath)) {
            downloadFile(downloadFilePath,in,conn.getContentLength());
            return;
        }
        else if(responseDataMode == ResponseDataMode.STREAM_ONLY) {
            result.setResponseStream(in);
            return;
        }
        else if(responseDataMode == ResponseDataMode.NONE) {
            return;
        }

        String responseStrData;
        //解读响应的输入流
        String encoding = conn.getContentEncoding();
        if(encoding!=null && encoding.equals("gzip")) {//响应体是否为gzip压缩
            responseStrData = StreamParser.readStreamFromGZIP(in);
        }
        else {
            responseStrData = StreamParser.readStream(in);
        }
        result.setData(responseStrData);
    }

    /**
     * 创建SSL连接工程
     */
    protected SSLSocketFactory createSSLSocketFactory(HttpsURLConnection httpsConn)
            throws NoSuchAlgorithmException, KeyManagementException
    {
        //创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm={new EmptyX509TrustManager()};
        SSLContext sslContext=SSLContext.getInstance("SSL");
        sslContext.init(null, tm, new SecureRandom());
        //从上述SSLContext对象中得到SSLSocketFactory对象
        return sslContext.getSocketFactory();
    }

    /**
     * 文件输出时判断Content-Type，这里仅用在输出的元信息里
     * 注：用到第三方组件：jmimemagic
     * @param file 要输出的文件
     */
    public String getContentTypeByFile(File file) {
        String fileContentType = "application/octet-stream";
        try {
            MagicMatch match = Magic.getMagicMatch(file,false);
            fileContentType=match.getMimeType();
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileContentType;
    }

    /**
     * 检查get方法下的参数是否正确
     */
    protected boolean checkParamsByGet(String params) {
        String regex="((\\w+={0,1})(\\w*)&{0,1})+";
        return Pattern.matches(regex,params);
    }

    /**
     * 检测连接协议，返回true代表是https协议
     */
    protected boolean isHttpsProtocol(URL url) throws IllegalArgumentException {
        String protocol = url.getProtocol().toLowerCase();
        boolean isHttpsProtocol;
        if(protocol.equals("https")) {
            isHttpsProtocol = true;
        }
        else if(protocol.equals("http")) {
            isHttpsProtocol = false;
        }
        else {
            throw new IllegalArgumentException("Url protocol is not http or https");
        }
        return isHttpsProtocol;
    }

    /**
     * 替换参数中的特殊字符
     */
    protected String replaceSpecialChar(String s) {
        String rslt=s;
        if(rslt.contains("+"))
            rslt=rslt.replace("+","%2B");
        else if(rslt.contains(" "))//空格
            rslt=rslt.replace(" ","%20");
        else if(rslt.contains("&"))
            rslt=rslt.replace("&","%26");
        else if(rslt.contains("="))
            rslt=rslt.replace("+","%3D");
        else if(rslt.contains("%"))
            rslt=rslt.replace("%","%25");
        else if(rslt.contains("/"))
            rslt=rslt.replace("/","%2F");
        else if(rslt.contains("?"))
            rslt=rslt.replace("?","%3F");
        else if(rslt.contains("#"))
            rslt=rslt.replace("#","%23");
        return rslt;
    }

    //原始不用到第三方组件版：文件输出时判断Content-Type
//    protected String getContentTypeForFile(File file) throws Exception{
//        String streamContentType = "application/octet-stream";
//        String imageContentType = "";
//        ImageInputStream image = null;
//        try {
//            image = ImageIO.createImageInputStream(file);
//            if (image == null) {
//                return streamContentType;
//            }
//            Iterator<ImageReader> it = ImageIO.getImageReaders(image);
//            if (it.hasNext()) {
//                imageContentType = "image/" + it.next().getFormatName();
//                return imageContentType;
//            }
//        } catch (IOException e) {
//            throw new Exception(e);
//        } finally {
//            try{
//                if (image != null) {
//                    image.close();
//                }
//            }catch(IOException e){
//                throw new Exception(e);
//            }
//        }
//        return streamContentType;
//    }

}
