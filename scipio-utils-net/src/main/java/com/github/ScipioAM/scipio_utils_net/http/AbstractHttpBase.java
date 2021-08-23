package com.github.ScipioAM.scipio_utils_net.http;

import com.github.ScipioAM.scipio_utils_io.parser.StreamParser;
import com.github.ScipioAM.scipio_utils_net.http.bean.RequestContent;
import com.github.ScipioAM.scipio_utils_net.http.bean.RequestInfo;
import com.github.ScipioAM.scipio_utils_net.http.bean.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.*;
import com.github.ScipioAM.scipio_utils_net.http.listener.*;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Java原生{@link HttpURLConnection}的易用性API父类
 * @author  Alan Scipio
 * @since 1.0.0
 * @date 2019/9/3
 */
public abstract class AbstractHttpBase implements IHttpRequester{

    //TODO [待完成]第一次自动下载服务器的SSL证书，存入cacerts里去

    //传输文件要用到
    protected final String END = "\r\n";
    //边界符-传输文件要用到
    protected final String BOUNDARY = "__END_OF_PART__";

    /** 请求的各种参数信息 */
    protected RequestInfo requestInfo = new RequestInfo();

    /** 请求内容 */
    protected RequestContent requestContent;

    /** 请求时上传的文件 */
    protected Map<String,File> uploadFiles;

    /** SSLContext初始化者 */
    protected SSLContextInitializer sslContextInitializer = SSLContextInitializer.DEFAULT;

    /** 信任管理器（决定了信任哪些SSL证书） */
    protected TrustManager[] trustManagers = new TrustManager[]{new AllTrustX509TrustManager()};

    protected UploadListener uploadListener;

    protected DownloadListener downloadListener;

    protected ResponseSuccessHandler responseSuccessHandler;

    protected ResponseFailureHandler responseFailureHandler;

    protected ExecuteErrorHandler executeErrorHandler;

    protected StartExecuteListener startExecuteListener;

    //==================================================================================================================

    /**
     * 发起http请求并获取返回的数据(不包括输出文件)
     * @param urlPath url路径
     * @param httpMethod get请求还是post请求
     * @param requestContent 请求的内容
     * @param responseDataMode 希望返回数据以什么形式封装
     * @return 响应数据
     */
    protected abstract ResponseResult doRequest(String urlPath, HttpMethod httpMethod, RequestContent requestContent, ResponseDataMode responseDataMode)
            throws IOException, IllegalArgumentException;

    /**
     * 发起http请求，上传文件（可能还包括其他参数），获取返回的数据
     * @param urlPath 请求的url
     * @param requestParams 请求的字符串参数
     * @param uploadFiles 要上传的文件
     * @param responseDataMode 希望返回数据以什么形式封装
     * @return 响应数据
     */
    protected abstract ResponseResult doFileRequest(String urlPath, Map<String,String> requestParams, Map<String,File> uploadFiles, ResponseDataMode responseDataMode)
            throws IOException, IllegalArgumentException;

    /**
     * 供子类简化调用：调用doRequest并作异常处理包装
     */
    protected ResponseResult execRequestAction(String urlPath, HttpMethod httpMethod, RequestContent requestContent, ResponseDataMode responseDataMode) {
        ResponseResult response;
        try {
            //开始执行前的回调
            if(startExecuteListener != null) {
                startExecuteListener.beforeExec(urlPath,httpMethod,requestInfo,requestContent,responseDataMode);
            }
            //执行
            response = doRequest(urlPath, httpMethod, requestContent, responseDataMode);
        }catch (Exception e) {
            if(executeErrorHandler != null) {
                executeErrorHandler.handle(urlPath,httpMethod,e);
            }
            response = new ResponseResult(ResponseResult.EXECUTE_ERR_CODE);
        }
        return response;
    }

    /**
     * 供子类简化调用：调用doFileRequest并作异常处理包装
     */
    protected ResponseResult execFileRequestAction(String urlPath, RequestContent requestContent, Map<String,File> uploadFiles, ResponseDataMode responseDataMode) {
        ResponseResult response;
        try {
            Map<String,String> requestParams = null;
            if(requestContent != null) {
                requestParams = requestContent.getFormContent();
            }
            //开始执行前的回调
            if(startExecuteListener != null) {
                startExecuteListener.beforeExec(urlPath,HttpMethod.POST,requestInfo,requestContent,responseDataMode);
            }
            //执行
            response = doFileRequest(urlPath,requestParams,uploadFiles,responseDataMode);
        }catch (Exception e) {
            if(executeErrorHandler != null)
                executeErrorHandler.handle(urlPath,HttpMethod.POST,e);
            response = new ResponseResult(ResponseResult.EXECUTE_ERR_CODE);
        }
        return response;
    }

    //==================================================================================================================

    @Override
    public ResponseResult get(String urlPath, ResponseDataMode responseDataMode) {
        return execRequestAction(urlPath, HttpMethod.GET,requestContent,responseDataMode);
    }

    @Override
    public ResponseResult get(String urlPath) {
        return get(urlPath,ResponseDataMode.DEFAULT);
    }

    @Override
    public ResponseResult post(String urlPath, ResponseDataMode responseDataMode) {
        return execRequestAction(urlPath, HttpMethod.POST,requestContent,responseDataMode);
    }

    @Override
    public ResponseResult post(String urlPath) {
        return post(urlPath,ResponseDataMode.DEFAULT);
    }

    @Override
    public ResponseResult postFile(String urlPath, ResponseDataMode dataMode) {
        return execFileRequestAction(urlPath,requestContent,uploadFiles,dataMode);
    }

    @Override
    public ResponseResult postFile(String urlPath) {
        return postFile(urlPath,ResponseDataMode.DEFAULT);
    }

    @Override
    public ResponseResult download(String urlPath, String downloadFileDir, String fileName) {
        String fileFullPath = downloadFileDir + File.separator + fileName;
        this.setDownloadFilePath(fileFullPath);
        return execRequestAction(urlPath, HttpMethod.POST,requestContent,ResponseDataMode.DOWNLOAD_FILE);
    }

    //==================================================================================================================

    /**
     * 下载文件
     * @param outputFilePath 要输出的文件（全路径）
     * @param in 连接里响应的输入流，文件数据在此流中
     * @param contentLength 响应的文件字节大小
     */
    protected void downloadFile(String outputFilePath, InputStream in, long contentLength) {
        IOException ioe = null;
        boolean isSuccess = false;
        File outputFile = new File(outputFilePath);
        try (BufferedInputStream bis = new BufferedInputStream(in); FileOutputStream fos = new FileOutputStream(outputFile)) {
            int length;//每次读取的字节数
            BigDecimal totalLength = new BigDecimal(contentLength);//总字节数
            BigDecimal readLength = BigDecimal.ZERO;//总共已读取的字节数
            byte[] buffer = new byte[getFileBufferSize()];
            while ((length = bis.read(buffer)) != -1) {
                fos.write(buffer,0,length);
                //下载中的回调
                if(downloadListener!=null) {
                    //计算下载进度
                    readLength = readLength.add(new BigDecimal(length));
                    BigDecimal downloadedPercent = readLength.divide(totalLength, RoundingMode.HALF_UP);
                    downloadListener.onDownloading(contentLength,readLength.longValue(),downloadedPercent);
                }
            }
            isSuccess = true;
        }catch (IOException e) {
            ioe = e;
        }
        //下载结束时的回调
        if(downloadListener!=null) {
            downloadListener.onFinished(isSuccess,outputFile,ioe);
        }
    }

    /**
     * 执行文件等数据的输出操作（上传）
     * @param os 连接的输出流对象
     * @param outputParams 要输出的字符串参数（可选）
     * @param outputFiles 要输出的文件对象（单个File对象或key-File的HashMap对象）
     */
    protected void doMultipartOutput(OutputStream os , Map<String,String> outputParams, Map<String,File> outputFiles)
            throws IOException
    {
        // 往服务器端写内容 也就是发起http请求需要带的参数
        DataOutputStream dos = new DataOutputStream(os);
        // 请求参数部分
        writeParams(outputParams, dos);
        // 请求上传文件部分
        writeFiles(outputFiles, dos);
        // 请求结束标志
        String endTarget = BOUNDARY + END;
        dos.writeBytes(endTarget);
        dos.flush();
        dos.close();
    }

    /**
     * 传输文件专用 - 对字符串参数进行编码处理并输出数据流中
     * @param outputParams 要传输的参数
     * @param dos 数据输出流
     */
    private void writeParams(Map<String,String> outputParams, DataOutputStream dos)
            throws IOException
    {
        if( outputParams==null || outputParams.isEmpty() ) {
            System.out.println("发送的字符串参数为空");//test
            return;
        }

        StringBuilder params=new StringBuilder();
        for( Map.Entry<String,String> entry : outputParams.entrySet() ) {
            //每段开头
            params.append(BOUNDARY).append(END);
            //参数头
            params.append("Content-Disposition: form-data; name=\"")
                    .append(entry.getKey()).append("\"");
            params.append(END);
            params.append("Content-Type: text/plain; charset=").append(requestInfo.getCharset());
            params.append(END);
            params.append(END);// 参数头设置完以后需要两个换行，然后才是参数内容
            params.append(entry.getValue());
            params.append(END);
        }
        dos.writeBytes(params.toString());
        dos.flush();
    }

    /**
     * 传输文件专用 - 将文件输出到数据流中
     * @param uploadFiles 要上传的文件
     * @param dos 数据输出流
     */
    private void writeFiles(Map<String,File> uploadFiles, DataOutputStream dos) throws IOException {
        if (uploadFiles == null) {
            return;
        }
        try {
            checkPostFile(uploadFiles);

            int loopCount = 1;
            for (Map.Entry<String, File> entry : uploadFiles.entrySet()) {
                //单个文件的输出（上传）
                writeSingleFile(loopCount, dos, entry.getKey(), entry.getValue());
                if (uploadListener != null) {
                    uploadListener.onFilesUploading(uploadFiles.size(), loopCount);
                }
                loopCount++;
            }//end of for
            //上传完成的回调
            if(uploadListener!=null) {
                uploadListener.onCompleted(uploadFiles);
            }
        }catch (IOException e){
            if(uploadListener!=null) {
                uploadListener.onError(e);
            }
            throw e;
        }
    }//end of writeFiles()

    /**
     * 对单个文件的输出操作
     * @param no 循环的第几个文件
     * @param dos 数据输出流
     * @param key 元信息里的name的值
     * @param file 要输出的文件
     */
    private void writeSingleFile(int no, DataOutputStream dos, String key, File file)
            throws IOException
    {

        String headParams = BOUNDARY + END +
                "Content-Disposition: form-data; name=\"" +
                key + "\"; filename=\"" +
                file.getName() + "\"" +
                END +
                "Content-Type:" +
                getContentTypeByFile(file) +
                END +
                END;// 参数头设置完以后需要两个换行，然后才是参数内容
        dos.writeBytes(headParams);

        FileInputStream fis = new FileInputStream(file);
        int len;
        BigDecimal readLength = BigDecimal.ZERO;//已经读取的长度
        BigDecimal fileLength = new BigDecimal(file.length());//文件总长度
        //确定上传文件缓冲区的大小
        byte[] buffer = new byte[getFileBufferSize(file.length())];
        //开始上传
        while ((len = fis.read(buffer)) != -1) {
            dos.write(buffer, 0, len);
            //上传进度的回调-针对单个上传文件
            if(uploadListener!=null) {
                readLength = readLength.add(new BigDecimal(len));
                BigDecimal uploadedPercent = readLength.divide(fileLength, RoundingMode.HALF_UP);
                uploadListener.onSingleUploading(no, file, fileLength.longValue(), readLength.longValue(), uploadedPercent);
            }
        }
        dos.writeBytes(END);
        dos.flush();
    }

    //==================================================================================================================

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
     * 获取默认User-Agent
     */
    public String getDefaultUserAgent() {
        return PresetUserAgent.UA_CHROME66_MAC;
    }

    /**
     * 获取当前设置的编码字符集
     */
    public String getCharset() {
        return requestInfo.getCharset();
    }

    /**
     * 获取当前设置的userAgent
     */
    public String getUserAgent() {
        return requestInfo.getUserAgent();
    }

    /**
     * 获取自定义请求头参数
     */
    public Map<String, String> getRequestHeaders() {
        return requestInfo.getRequestHeaders();
    }

    /**
     * 检查当前设置的followRedirects参数情况
     * 参数含义：是否关闭重定向以获取跳转后的真实地址
     */
    public boolean isFollowRedirects() {
        return requestInfo.isFollowRedirects();
    }

    /**
     * 清除所有监听器
     */
    public void clearListeners() {
        downloadListener = null;
        uploadListener = null;
        responseSuccessHandler = null;
        responseFailureHandler = null;
        executeErrorHandler = null;
    }

    /**
     * 获取当前设置的 上传文件缓冲区大小 如果为空则取默认值
     * @param fileLength 文件总长度，单位是byte
     */
    public int getFileBufferSize(long fileLength) {
        if (requestInfo.getFileBufferSize() == null || requestInfo.getFileBufferSize() == 0) {
            setDefaultBufferSize(fileLength);
        }
        return requestInfo.getFileBufferSize();
    }

    public int getFileBufferSize() {
        if(requestInfo.getFileBufferSize()==null || requestInfo.getFileBufferSize()==0) {
            setDefaultBufferSize();
        }
        return requestInfo.getFileBufferSize();
    }

    /**
     * 根据文件大小调整缓冲区的大小
     * @param fileLength 文件总长度，单位是byte
     */
    public void setDefaultBufferSize(long fileLength) {
        if (fileLength <= (1024 * 1024)) {
            requestInfo.setFileBufferSize(1024);
        }
        else if (fileLength <= (1024 * 1024 * 10)) {
            requestInfo.setFileBufferSize(4096);
        }
        else {
            requestInfo.setFileBufferSize(1024 * 1024);
        }
    }

    public void setDefaultBufferSize() {
        requestInfo.setFileBufferSize(1024);
    }

    /**
     * 获取下载文件的全路径
     */
    public String getDownloadFilePath() {
        return requestInfo.getDownloadFilePath();
    }

    /**
     * 重置各项配置
     */
    public void resetConfig() {
        requestInfo.setUserAgent(null);
        requestInfo.setRequestHeaders(null);
        requestInfo.setProxy(null);
        requestInfo.setFollowRedirects(false);
        requestInfo.setFileBufferSize(null);
        requestInfo.setDownloadFilePath(null);
        clearListeners();
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public TrustManager[] getTrustManagers() {
        return trustManagers;
    }

    @Override
    public AbstractHttpBase setTrustManagers(TrustManager... trustManagers) {
        this.trustManagers = trustManagers;
        return this;
    }

    @Override
    public AbstractHttpBase setSSLContextInitializer(SSLContextInitializer sslContextInitializer) {
        this.sslContextInitializer = sslContextInitializer;
        return this;
    }

    public SSLContextInitializer getSslContextInitializer() {
        return sslContextInitializer;
    }

    @Override
    public AbstractHttpBase setUploadListener(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
        return this;
    }

    @Override
    public AbstractHttpBase setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
        return this;
    }

    @Override
    public AbstractHttpBase setResponseSuccessHandler(ResponseSuccessHandler responseSuccessHandler) {
        this.responseSuccessHandler = responseSuccessHandler;
        return this;
    }

    @Override
    public AbstractHttpBase setResponseFailureHandler(ResponseFailureHandler responseFailureHandler) {
        this.responseFailureHandler = responseFailureHandler;
        return this;
    }

    @Override
    public AbstractHttpBase setExecuteErrorHandler(ExecuteErrorHandler executeErrorHandler) {
        this.executeErrorHandler = executeErrorHandler;
        return this;
    }

    @Override
    public IHttpRequester setStartExecuteListener(StartExecuteListener startExecuteListener) {
        this.startExecuteListener = startExecuteListener;
        return this;
    }

    //==================================================================================================================

    /**
     * 检查上传的文件，有问题就抛异常
     */
    protected void checkPostFile(Map<String,File> files) throws IllegalArgumentException{
        for( Map.Entry<String,File> entry : files.entrySet() ) {
            if( !entry.getValue().exists() || entry.getValue().isDirectory()) {
                throw new IllegalArgumentException("file not exists or is a directory which key is ["+entry.getKey()+"]");
            }
        }
    }

    /**
     * 设置http和https连接共通的部分
     * @param conn http或https连接对象
     * @param contentType 请求头的contentType
     * @param httpMethod get请求还是post请求
     */
    protected void setCommonConnectionData(HttpURLConnection conn, String contentType, HttpMethod httpMethod) throws ProtocolException {
        conn.setRequestMethod(httpMethod.value);//设置连接方法
        conn.setInstanceFollowRedirects(requestInfo.isFollowRedirects());//是否关闭重定向以获取跳转后的真实地址
        //设置共通的头信息，以及输出请求参数
        conn.setConnectTimeout(1000 * 10);//设置连接超时的时间(毫秒)
        conn.setReadTimeout(1000 * 60);
        conn.setDoInput(true);//设置连接打开输入流
        if(httpMethod == HttpMethod.POST) {
            conn.setDoOutput(true);//设置连接打开输出
        }
        conn.setRequestProperty("Accept-Encoding", "gzip,deflate");//告诉服务器支持gzip压缩
        conn.setRequestProperty("Accept-Charset", requestInfo.getCharset());
        conn.setRequestProperty("Charset", requestInfo.getCharset());
        //设置contentType
        if(contentType != null) {
            conn.setRequestProperty("Content-Type", contentType);//可被后面的headerParam覆盖
        }
        //设置user-agent，默认user-agent是Mac系统上66版的chrome浏览器
        if(requestInfo.getUserAgent() != null && (!"".equals(requestInfo.getUserAgent()))) {
            conn.setRequestProperty("User-Agent", requestInfo.getUserAgent());
        }
        //设置自定义头部参数
        if(requestInfo.getRequestHeaders() != null && requestInfo.getRequestHeaders().size() > 0) {
            for (Map.Entry<String, String> entry : requestInfo.getRequestHeaders().entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 输出请求内容（请求体）
     * @param conn 连接对象
     * @param httpMethod 请求方法（GET还是POST）
     * @param requestContent 请求内容
     * @throws IOException 输出异常
     */
    protected void outputRequestContent(HttpURLConnection conn, HttpMethod httpMethod, RequestContent requestContent) throws IOException {
        if(httpMethod == HttpMethod.GET || requestContent == null) {
            return;
        }
        OutputStream out = conn.getOutputStream();
        String outputContent = null;
        switch (requestContent.getMode()) {
            case FORM:
                outputContent = setContentForPost(requestContent.getFormContent());
                break;
            case TEXT_JSON:
            case TEXT_XML:
            case TEXT_PLAIN:
                outputContent = requestContent.getStrContent();
                break;
        }
        if(outputContent != null) {
            out.write(outputContent.getBytes(requestInfo.getCharset()));//将数据输出
            out.close();
        }
    }

    /**
     * 依据返回的状态码进行处理
     */
    protected ResponseResult handleResponse(ResponseResult result, int responseCode, ResponseDataMode responseDataMode, InputStream in, long contentLength, String encoding) throws IOException {
        if(responseCode >= 200 && responseCode < 300) {
            handleSuccess(result, responseDataMode, in, contentLength, encoding);
            //成功后的响应回调
            if(responseSuccessHandler != null) {
                responseSuccessHandler.handle(responseCode,result);
            }
        }
        else {
            //失败后的响应回调
            if(responseFailureHandler != null) {
                responseFailureHandler.handle(responseCode,result);
            }
        }
        return result;
    }

    /**
     * 响应成功时的处理（响应码:2xx）
     */
    protected void handleSuccess(ResponseResult result, ResponseDataMode responseDataMode, InputStream in, long contentLength, String encoding)
            throws IOException
    {
        if(responseDataMode == ResponseDataMode.DOWNLOAD_FILE) {
            if(requestInfo.getDownloadFilePath()==null || "".equals(requestInfo.getDownloadFilePath())) {
                throw new IOException("argument [downloadFilePath] not set!");
            }
            downloadFile(requestInfo.getDownloadFilePath(),in,contentLength);
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
        if(encoding!=null && encoding.equalsIgnoreCase("gzip")) {//响应体是否为gzip压缩
            responseStrData = StreamParser.readStreamFromGZIP(in);
        }
        else {
            responseStrData = StreamParser.readStream(in);
        }
        result.setData(responseStrData);
    }

    /**
     * 创建SSLSocket工厂以用于HTTPS连接
     */
    protected SSLSocketFactory createSSLSocketFactory()
            throws Exception
    {
        if(sslContextInitializer == null) {
            sslContextInitializer = SSLContextInitializer.DEFAULT;
        }
        SSLContext sslContext = sslContextInitializer.build(trustManagers);
        //从上述SSLContext对象中得到SSLSocketFactory对象
        return sslContext.getSocketFactory();
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
     * 准备最终的url
     * @param urlPath 原始传入的url
     * @param httpMethod http方法（get还是post）
     * @param content 请求内容
     * @return 最终连接用的url
     */
    protected String prepareFinalUrl(String urlPath, HttpMethod httpMethod, RequestContent content) {
        String currentUrl = urlPath;
        if(content == null || httpMethod == HttpMethod.POST) {
            return currentUrl;
        }
        RequestDataMode dataMode = content.getMode();
        //为get方法拼接参数
        if(dataMode == RequestDataMode.FORM) {
            currentUrl = setUrlByGet(urlPath, content.getFormContent());
        }
        else if(dataMode == RequestDataMode.TEXT_PLAIN) {
            checkParamsByGet(content.getStrContent());
            currentUrl += ("?" + content.getStrContent());
        }
        else {
            throw new IllegalArgumentException("Wrong requestDataMode when using GET method! requestDataMode: [" + dataMode.name() + "]");
        }
        return currentUrl;
    }

    /**
     * 检查get方法下的参数是否正确
     */
    private void checkParamsByGet(String params) throws IllegalArgumentException {
        String regex="((\\w+=?)(\\w*)&?)+";
        if(!Pattern.matches(regex,params)) {
            throw new IllegalArgumentException("Wrong request params when using GET method");
        }
    }

    /**
     * 为get方法准备好带参的url地址
     * @param address 地址
     * @param params 参数
     * @return 一拼接好的地址字符串
     */
    private String setUrlByGet(String address, Map<String,String> params)
    {
        if(params==null) {
            return address;
        }

        String url=address+"?";
        StringBuilder temp=new StringBuilder();
        for(Map.Entry<String,String> p : params.entrySet()) {
            temp.append(replaceSpecialChar(p.getKey()));
            temp.append("=");
            temp.append(replaceSpecialChar(p.getValue()));
            temp.append("&");
        }
        url += temp.substring(0, (temp.length() - 1));
        return url;
    }

    /**
     * 将参数对象转换成适应post提交的字符串
     * @param params 需要传递的参数
     * @return 已经转换适成应post提交的字符串
     */
    protected String setContentForPost(Map<String,String> params)
    {
        if(params==null) {
            throw new IllegalArgumentException("params is null");
        }

        StringBuilder temp=new StringBuilder();
        String postData;
        for(Map.Entry<String,String> p : params.entrySet()) {
            temp.append(replaceSpecialChar(p.getKey()));
            temp.append("=");
            temp.append(replaceSpecialChar(p.getValue()));
            temp.append("&");
        }
        postData = temp.substring(0, temp.length() - 1);
        return postData;
    }

    /**
     * 替换参数中的特殊字符
     */
    private String replaceSpecialChar(String s) {
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

}
