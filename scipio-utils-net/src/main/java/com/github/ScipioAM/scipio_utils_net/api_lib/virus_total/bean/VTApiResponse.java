package com.github.ScipioAM.scipio_utils_net.api_lib.virus_total.bean;

import com.github.ScipioAM.scipio_utils_net.api_lib.ApiResponseBase;

/**
 * VirusTotal的API响应结果
 * @author Alan Scipio
 * @since 2021/7/14
 */
public class VTApiResponse extends ApiResponseBase {

    private VTData data;

    public VTData getData() {
        return data;
    }

    public void setData(VTData data) {
        this.data = data;
    }

}
