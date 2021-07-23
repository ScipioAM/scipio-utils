package com.github.ScipioAM.scipio_utils_net.api_lib.virus_total;

import com.github.ScipioAM.scipio_utils_net.api_lib.ApiUtilBaseEnhance;
import com.github.ScipioAM.scipio_utils_net.api_lib.virus_total.bean.VTApiResponse;

/**
 * VirusTotal的API调用工具
 * <p>API文档网址：https://developers.virustotal.com/v3.0/reference</p>
 * @author Alan Scipio
 * @since 2021/7/14
 */
public class VTApiUtil extends ApiUtilBaseEnhance<VTApiResponse,VTApiUtil> {

    public VTApiUtil() {
        authenticationKey = "x-apikey"; //VirusTotal的apiKey名称
    }

    //TODO 更多VirusTotal专有的操作有待添加

}
