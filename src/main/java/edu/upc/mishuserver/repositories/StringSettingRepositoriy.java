package edu.upc.mishuserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.upc.mishuserver.dto.StringSetting;

/**
 * StringSettingRepositoriy
 */
public interface StringSettingRepositoriy extends JpaRepository<StringSetting,Long>{

    
}