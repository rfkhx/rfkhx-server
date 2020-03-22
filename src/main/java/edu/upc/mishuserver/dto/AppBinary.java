package edu.upc.mishuserver.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AppBinary
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppBinary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long versioncode;
    private String versionname;
    private String platform;
    private String filename;
    private String description;
    private String md5;
    private Long size;
}