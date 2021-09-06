package com.github.ScipioAM.scipio_utils_common.os;

import com.github.ScipioAM.scipio_utils_common.AssertUtil;
import com.github.ScipioAM.scipio_utils_common.os.bean.WinServiceOption;
import com.github.ScipioAM.scipio_utils_common.os.bean.WinServiceResult;
import com.github.ScipioAM.scipio_utils_common.os.constants.SCType;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Windows Service相关(运行需要管理员权限)
 * @author Alan Scipio
 * @since 1.0.2-p3
 * @date 2021/9/1
 */
public class WindowsService extends AbstractWindowsExecutor {

    private WinServiceResultHandler resultHandler;

    private WinServiceSuccessHandler successHandler;

    private WindowsService() {}

    public static WindowsService newInstance() {
        return new WindowsService();
    }

    //==================================================================================================================

    /**
     * 查询指定service的当前状态
     * @param serviceName 指定service的名字
     * @return service当前的状态，执行失败则返回null
     */
    public WinServiceResult query(String serviceName) {
        AssertUtil.notNull(serviceName);
        String cmd = "sc query " + serviceName;
        return exec(SCType.QUERY,cmd);
    }

    /**
     * 启动指定的service
     * @param serviceName 指定service的名字
     * @return service当前的状态，执行失败则返回null
     */
    public WinServiceResult start(String serviceName) {
        AssertUtil.notNull(serviceName);
        String cmd = "sc start " + serviceName;
        return exec(SCType.START,cmd);
    }

    /**
     * 停止指定的service
     * @param serviceName 指定service的名字
     * @return service当前的状态，执行失败则返回null
     */
    public WinServiceResult stop(String serviceName) {
        AssertUtil.notNull(serviceName);
        String cmd = "sc stop " + serviceName;
        return exec(SCType.STOP,cmd);
    }

    /**
     * 构建任意名称的sid
     * @param anyName 任意名称
     * @return 构建结果
     */
    public WinServiceResult showSid(String anyName) {
        AssertUtil.notNull(anyName);
        String cmd = "sc showsid " + anyName;
        return exec(SCType.SHOW_SID,cmd);
    }

    /**
     * 创建windows服务
     * @param option 创建的相关配置
     * @return 创建结果
     */
    public WinServiceResult create(WinServiceOption option) {
        AssertUtil.notNull(option);
        String cmd = "sc create ";//TODO 1.如何构建create命令待完成
        return exec(SCType.CREATE,cmd);
    }

    /**
     * create方法的别名
     */
    public WinServiceResult install(WinServiceOption option) {
        return create(option);
    }

    /**
     * 删除指定的windows服务
     *      <p>注：如果服务正在运行，或者另一个进程具有服务的打开句柄，则服务将标记为要删除</p>
     * @param serviceName 指定service的名字
     * @return 删除结果
     */
    public WinServiceResult delete(String serviceName) {
        AssertUtil.notNull(serviceName);
        String cmd = "sc delete " + serviceName;
        return exec(SCType.DELETE,cmd);
    }

    /**
     * delete方法的别名
     */
    public WinServiceResult uninstall(String serviceName) {
        return delete(serviceName);
    }

    /**
     * 设置指定service的描述
     * @param serviceName 指定service的名字
     * @param description 描述，为null或空串则等于
     * @return 执行结果
     */
    public WinServiceResult setDescription(String serviceName, String description) {
        AssertUtil.notNull(serviceName);
        if(description == null) {
            description = "";
        }
        String cmd = "sc description " + serviceName + " \"" + description + "\"";
        return exec(SCType.DELETE,cmd);
    }

    //==================================================================================================================

    /**
     * 执行sc相关命令并解析结果
     * @param scType sc命令类型
     * @param cmd 要执行的命令
     * @return 解析后的执行结果（通常是service当前的状态）
     */
    private WinServiceResult exec(SCType scType, String cmd) {
        //执行
        List<String> resultList = WindowsCmd.execute_list(cmd);
        if(resultList == null || resultList.size() <= 0) { //执行失败，返回null
            return null;
        }
        //处理返回消息
        return analyseResult(scType,cmd,resultList);
    }

    /**
     * 解析sc相关命令的结果（预期结果是service的状态信息）
     * @param resultList 执行返回的信息
     * @return 解析后的结果对象，解析失败则返回null
     */
    private WinServiceResult analyseResult(SCType scType, String cmd, List<String> resultList) {
        WinServiceResult result = new WinServiceResult();
        StringBuilder originalMsg = new StringBuilder();
        try {
            System.out.println("Execute command: " + cmd);
            if(resultHandler != null) { //自定义结果处理器
                result = resultHandler.handle(scType, cmd, resultList);
            }
            else {
                for(String line : resultList) {
                    originalMsg.append(line).append("\n");
                }
                originalMsg.deleteCharAt(originalMsg.length() - 1);
                result.setOriginalMsg(originalMsg.toString());
                //执行成功
                if(checkResult(scType,resultList)) {
                    result.setExecSuccess(true);
                    if(successHandler != null) { //自定义的执行成功处理器
                        result = successHandler.handle(scType,resultList,result);
                    }
                    else {
                        switch (scType) {
                            case QUERY:
                                result = WinServiceSuccessHandler.QUERY.handle(scType,resultList,result);
                                break;
                            case START:
                                result = WinServiceSuccessHandler.START.handle(scType,resultList,result);
                                break;
                            case STOP:
                                result = WinServiceSuccessHandler.STOP.handle(scType,resultList,result);
                                break;
                            case SHOW_SID:
                                result = WinServiceSuccessHandler.SHOW_SID.handle(scType,resultList,result);
                                break;
                            case CREATE:
                                result = WinServiceSuccessHandler.CREATE.handle(scType,resultList,result);
                                break;
                            case DELETE:
                                result = WinServiceSuccessHandler.DELETE.handle(scType,resultList,result);
                                break;
                            default: //DESCRIPTION命令不需要进一步解析
                                break;
                        }
                    }
                }
                else { //执行得到失败结果
                    result.setExecSuccess(false);
                }
            }
        }catch (Exception e) {
            result = null;
            e.printStackTrace();
            System.err.println("Execute command failed, command: [" + cmd + "] originalMsg: [" + originalMsg + "]");
        }
        return result;
    }

    /**
     * 判断执行结果是否为成功
     * @param scType sc命令类型
     * @param resultList 原始结果集
     * @return true代表成功
     */
    private boolean checkResult(SCType scType, List<String> resultList) {
        if(scType == SCType.DESCRIPTION) {
            String line = resultList.get(0);
            return (line.contains("成功") || line.contains("success"));
        }
        else if(scType == SCType.SHOW_SID) {
            return (resultList.size() == 3);
        }
        else {
            return (resultList.size() > 4);
        }
    }

    /**
     * 正则解析结果行里的代码
     *      <p>例如：[        STATE              : 4  RUNNING]</p>
     *      <p>或：[        WIN32_EXIT_CODE    : 0  (0x0)]等</p>
     * @param line 要解析的结果行
     * @return 结果行里的代码，如果正则匹配失败就返回null
     */
    public static Integer analyzeCode(String line) {
        Pattern pattern = Pattern.compile("(\\s+\\S+\\s+:\\s)(\\w{1,5})(.*)");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()) {
            try {
                return Integer.decode(matcher.group(2));
            }catch (Exception e) {
                System.err.println("Can not convert string to number: [" + line + "]");
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    //==================================================================================================================

    public WindowsService setResultHandler(WinServiceResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        return this;
    }

    public WindowsService setSuccessHandler(WinServiceSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }
}
