package edu.upc.mishuserver.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * 用于支持“记住我”功能
 */
@Entity(name = "persistent_logins")
public class MyPersistentRememberMeToken {
    @NotNull
    private String username;
    @Id
	private String series;
    @NotNull
    private String token;
    @NotNull
    private Timestamp last_used;
}