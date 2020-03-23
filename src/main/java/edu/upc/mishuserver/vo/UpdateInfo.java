package edu.upc.mishuserver.vo;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UpdateInfo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateInfo {

    @JSONField(name="Code")
    private Long code;
    @JSONField(name = "Msg")
    private String msg;
    @JSONField(name = "UpdateStatus")
    private Integer updateStatus;
    @JSONField(name = "VersionCode")
    private Long versionCode;
    @JSONField(name = "VersionName")
    private String versionName;
    @JSONField(name = "ModifyContent")
    private String modifyContent;
    @JSONField(name = "DownloadUrl")
    private String downloadUrl;
    @JSONField(name = "ApkSize")
    private Long apkSize;
    @JSONField(name = "ApkMd5")
    private String apkMd5;
}