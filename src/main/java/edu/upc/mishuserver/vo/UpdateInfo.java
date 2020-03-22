package edu.upc.mishuserver.vo;

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

    private Long Code;
    private String Msg;
    private Integer UpdateStatus;
    private Long VersionCode;
    private String VersionName;
    private String ModifyContent;
    private String DownloadUrl;
    private Long ApkSize;
    private String ApkMd5;
}