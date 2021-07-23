package com.github.ScipioAM.scipio_utils_net.http;

import com.github.ScipioAM.scipio_utils_net.http.common.RequestDataMode;
import com.github.ScipioAM.scipio_utils_net.http.common.RequestMethod;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseResult;
import com.github.ScipioAM.scipio_utils_net.http.common.ResponseDataMode;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class: HttpConnectionBase
 * Description:
 * Author: Alan Min
 * Create Time: 2018/6/28
 */
public abstract class AbstractHttpUtil extends AbstractHttpBase {

    //传输文件要用到
    private final String TOW_HYPHENS = "--";
    private final String END = "\r\n";
    //边界符-传输文件要用到
    private String BOUNDARY = null;

    //----------------------------------------------------------------------------------------------

    /**
     * 发起http请求并获取返回的数据(不包括输出文件)
     * @param urlPath url路径
     * @param requestMethod get请求还是post请求
     * @param requestDataMode 发起请求时传参的方式，默认(比如x=1&y=2...)
     * @param responseDataMode 封装响应数据的模式
     * @param requestParam 请求时发送的数据
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ResponseResult doRequest(String urlPath, RequestMethod requestMethod,
                                       RequestDataMode requestDataMode, ResponseDataMode responseDataMode, Object requestParam)
            throws IOException, IllegalArgumentException, KeyManagementException
    {
        ResponseResult result;//连接后获取的响应结果
        String currentOutputString = null;//要输出的字符串数据(可选)
        String currentUrl = urlPath;//最终的url地址
        String contentType = null;
        //准备请求参数
        if(requestMethod== RequestMethod.GET)//为get方法拼接参数
        {
            if(requestParam!=null)
            {
                if(requestParam instanceof String)
                {
                    if( super.checkParamsByGet((String)requestParam))
                        currentUrl += ("?"+requestParam);
                    else
                        throw new IllegalArgumentException("Wrong params by using GET method");
                }
                else if (requestParam instanceof HashMap)
                {
                    currentUrl = setUrlByGet(urlPath, (HashMap<String,String>)requestParam);
                }
                else
                {
                    throw new IllegalArgumentException("Wrong params by using GET method");
                }
            }
        }
        else//为post方法准备数据
        {
            switch (requestDataMode)
            {
                case DEFAULT:
                    if(requestParam instanceof String)
                        currentOutputString = (String) requestParam;
                    else if(requestParam instanceof HashMap)
                        currentOutputString = setParamsByPost((HashMap<String,String>)requestParam);
                    else
                        throw new IllegalArgumentException("Wrong request params");
                    break;
                case JSON:
                    contentType = "application/json";
                    currentOutputString = (String) requestParam;
                    break;
                case XML:
                    contentType = "text/xml";
                    currentOutputString = (String) requestParam;
                    break;
            }
        }

        URL url= new URL(currentUrl);
        //检查是http还是https
        if(isHttpsProtocol(url))//如果是https必要的处理
        {
            HttpsURLConnection httpsConn;
            if(proxy!=null)
                httpsConn= (HttpsURLConnection) url.openConnection(proxy);
            else
                httpsConn= (HttpsURLConnection) url.openConnection();

            try {
                httpsConn.setSSLSocketFactory(super.createSSLSocketFactory(httpsConn));//设置SSLSocketFactory
            }catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            httpsConn.setRequestMethod(requestMethod.getValue());//设置连接方法
            httpsConn.setInstanceFollowRedirects(isFollowRedirects);//是否关闭重定向以获取跳转后的真实地址
            //公共的请求头设置
            setCommonConnectionData(httpsConn,contentType,requestMethod);

            //如果是非默认传参方式且为POST请求，则通过输出流输出参数
            if(requestMethod== RequestMethod.POST && (requestParam!=null) )
            {
                OutputStream os = httpsConn.getOutputStream();
                assert currentOutputString != null;
                os.write(currentOutputString.getBytes(StandardCharsets.UTF_8));//将数据输出
                os.close();
            }

            //处理响应结果
            int responseCode = httpsConn.getResponseCode();//获取返回的状态码
            result = super.handleResponse(httpsConn,responseCode, responseDataMode);//获取返回的数据
        }
        else
        {
            HttpURLConnection httpConn;
            if(proxy!=null)
                httpConn= (HttpURLConnection) url.openConnection(proxy);
            else
                httpConn= (HttpURLConnection) url.openConnection();

            httpConn.setRequestMethod(requestMethod.getValue());//设置连接方法
            httpConn.setInstanceFollowRedirects(isFollowRedirects);//是否关闭重定向以获取跳转后的真实地址
            //公共设置
            super.setCommonConnectionData(httpConn,contentType,requestMethod);

            //如果是非默认传参方式且为POST请求，则通过输出流输出参数
            if( (requestMethod== RequestMethod.POST) && (requestParam!=null) )
            {
                OutputStream os = httpConn.getOutputStream();
                assert currentOutputString != null;
                os.write(currentOutputString.getBytes(StandardCharsets.UTF_8));//将数据输出
                os.close();
            }

            //处理响应结果
            int responseCode = httpConn.getResponseCode();//获取返回的状态码
            result = handleResponse(httpConn,responseCode, responseDataMode);//获取返回的数据
        }
        return result;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 发起http请求，输出文件流（可能还包括其他参数），获取返回的数据
     * @param urlPath 请求的url
     * @param requestParams 请求的字符串参数,不需要传递时为null
     * @param outputFiles 输出的文件（单个File对象或key-File的HashMap对象）
     * @param responseDataMode 封装响应数据的模式
     * @return 响应数据
     */
    @Override
    protected ResponseResult doFileRequest(String urlPath, Map<String,String> requestParams, Object outputFiles , ResponseDataMode responseDataMode )
            throws IOException, KeyManagementException, IllegalArgumentException {
        //返回的响应结果
        ResponseResult result;
        //定义边界符
        BOUNDARY = "**********"+System.currentTimeMillis();
        //Content-Type的定义
        String contentType = "multipart/form-data; boundary="+BOUNDARY;

        URL url = new URL(urlPath);
        //https的情况下
        if(isHttpsProtocol(url))
        {
            HttpsURLConnection httpsConn;
            if(proxy!=null)
                httpsConn = (HttpsURLConnection) url.openConnection(proxy);
            else
                httpsConn = (HttpsURLConnection) url.openConnection();

            try {
                httpsConn.setSSLSocketFactory(super.createSSLSocketFactory(httpsConn));//设置SSLSocketFactory
            }catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            httpsConn.setInstanceFollowRedirects(isFollowRedirects);//是否关闭重定向以获取跳转后的真实地址
            httpsConn.setRequestMethod(RequestMethod.POST.getValue());
            httpsConn.setUseCaches(false);
            //公共的请求头设置
            setCommonConnectionData(httpsConn,contentType, RequestMethod.POST);

            //执行数据输出操作
            doMultipartOutput(httpsConn.getOutputStream(), requestParams, outputFiles);

            //获取响应码
            int responseCode = httpsConn.getResponseCode();
            //处理返回的数据
            result = super.handleResponse(httpsConn,responseCode, responseDataMode);
        }
        //http的情况下
        else
        {
            HttpURLConnection httpConn;
            if(proxy!=null)
                httpConn = (HttpsURLConnection) url.openConnection(proxy);
            else
                httpConn = (HttpsURLConnection) url.openConnection();

            httpConn.setInstanceFollowRedirects(isFollowRedirects);//是否关闭重定向以获取跳转后的真实地址
            httpConn.setRequestMethod(RequestMethod.POST.getValue());

            //执行数据输出操作
            doMultipartOutput(httpConn.getOutputStream(), requestParams, outputFiles);

            //获取响应码
            int responseCode = httpConn.getResponseCode();
            //处理返回的数据
            result = super.handleResponse(httpConn,responseCode, responseDataMode);
        }
        return result;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 执行数据输出操作
     * @param os 连接的输出流对象
     * @param outputParams 要输出的字符串参数（可选）
     * @param outputFiles 要输出的文件对象（单个File对象或key-File的HashMap对象）
     */
    private void doMultipartOutput( OutputStream os , Map<String,String> outputParams, Object outputFiles)
            throws IOException
    {
        // 往服务器端写内容 也就是发起http请求需要带的参数
        DataOutputStream dos = new DataOutputStream(os);
        // 请求参数部分
        writeParams(outputParams, dos);
        // 请求上传文件部分
        writeFiles(outputFiles, dos);
        // 请求结束标志
        String endTarget = TOW_HYPHENS + BOUNDARY + TOW_HYPHENS + END;
        dos.writeBytes(endTarget);
        dos.flush();
        dos.close();
    }

    /**
     * 传输文件专用 - 对字符串参数进行编码处理并输出数据流中
     * @param outputParams 要传输的参数
     * @param dos 数据输出流
     */
    private void writeParams(Map<String,String> outputParams, DataOutputStream dos) throws IOException
    {
        if( outputParams==null || outputParams.isEmpty() )
        {
            System.out.println("发送的字符串参数为空");//test
            return;
        }

        StringBuilder params=new StringBuilder();
        for( Map.Entry<String,String> entry : outputParams.entrySet() )
        {
            //每段开头
            params.append(TOW_HYPHENS).append(BOUNDARY).append(END);
            //参数头
            params.append("Content-Disposition: form-data; name=\"")
                    .append(entry.getKey()).append("\"");
            params.append(END);
            params.append("Content-Type: text/plain; charset=utf-8");
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
     * @param outputFiles 要输出的文件（单个File对象或key-File的HashMap对象）
     * @param dos 数据输出流
     */
    @SuppressWarnings("unchecked")
    private void writeFiles(Object outputFiles, DataOutputStream dos) throws IOException
    {
        if(outputFiles==null )
        {
            return;
        }
        try {
            if( outputFiles instanceof File )
            {
                File file=(File)outputFiles;
                checkPostFile(file);
                writeSingleFile(dos,"file",file,true);
                //上传完成的回调
                if(uploadListener!=null)
                {
                    uploadListener.onCompleted();
                }
            }
            else if( outputFiles instanceof HashMap )
            {
                HashMap<String,File> fileMap=(HashMap<String,File>)outputFiles;
                checkPostFile(fileMap);

                double loopCount=1.0;
                boolean isSingleFile = ( fileMap.size()==1 );
                for( Map.Entry<String,File> entry : fileMap.entrySet() )
                {
                    writeSingleFile(dos,entry.getKey(),entry.getValue(),isSingleFile);
                    if( !isSingleFile && uploadListener!=null)
                    {
                        double percentage=loopCount/fileMap.size();
                        uploadListener.onProcess(percentage);
                    }
                    loopCount++;
                }//end of for
                //上传完成的回调
                if(uploadListener!=null)
                {
                    uploadListener.onCompleted();
                }
            }//end of elseif
        }catch (IOException e){
            if(uploadListener!=null)
            {
                uploadListener.onError(e);
            }
            throw e;
        }
    }//end of writeFiles()

    /**
     * 对单个文件的输出操作
     * @param dos 数据输出流
     * @param key 元信息里的name的值
     * @param file 要输出的文件
     * @param isSingleUpload 是否是单个文件输出（关系到响应监听器）
     */
    private void writeSingleFile( DataOutputStream dos, String key, File file , boolean isSingleUpload)
            throws IOException
    {

        String headParams = TOW_HYPHENS + BOUNDARY + END +
                "Content-Disposition: form-data; name=\"" +
                key + "\"; filename=\"" +
                file.getName() + "\"" +
                END +
                "Content-Type:" +
                super.getContentTypeByFile(file) +
                END +
                END;// 参数头设置完以后需要两个换行，然后才是参数内容
        dos.writeBytes(headParams);

        FileInputStream fis = new FileInputStream(file);
        int len;
        int readedLen=0;//已经读取的长度
        double fileLength=file.length();//文件总长度
        //确定上传文件缓冲区的大小
        byte[] buffer = new byte[getFileBufferSize(file.length())];
        //开始上传
        while ((len = fis.read(buffer)) != -1) {
            dos.write(buffer, 0, len);
            readedLen += len;//已输出的长度
            //上传进度的回调-针对单个上传文件
            if(isSingleUpload && uploadListener!=null)
            {
                double percentage=readedLen/fileLength;
                uploadListener.onProcess(percentage);
            }
        }
        dos.writeBytes(END);
        dos.flush();
    }

    /**
     * 为get方法准备好带参的url地址
     * @param address 地址
     * @param params 参数
     * @return 一拼接好的地址字符串
     */
    private String setUrlByGet(String address, Map<String,String> params)
    {
        if(params==null)
            return address;

        String url=address+"?";
        StringBuilder temp=new StringBuilder();
        for(Map.Entry<String,String> p : params.entrySet())
        {
            temp.append( super.replaceSpecialChar(p.getKey()));
            temp.append("=");
            temp.append( super.replaceSpecialChar(p.getValue()));
            temp.append("&");
        }
        url+=temp.substring(0,  (temp.length()-1) );
        return url;
    }


    /**
     * 将参数对象转换成适应post提交的字符串
     * @param params 需要传递的参数
     * @return 已经转换适成应post提交的字符串
     */
    private String setParamsByPost(Map<String,String> params)
    {
        if(params==null)
            throw new IllegalArgumentException("params is null");

        StringBuilder temp=new StringBuilder();
        String postData;
        for(Map.Entry<String,String> p : params.entrySet())
        {
            temp.append( super.replaceSpecialChar(p.getKey()));
            temp.append("=");
            temp.append( super.replaceSpecialChar(p.getValue()));
            temp.append("&");
        }
        postData=temp.substring(0 , temp.length()-1);
        return postData;
    }

}
