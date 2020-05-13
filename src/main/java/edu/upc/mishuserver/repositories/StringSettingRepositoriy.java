package edu.upc.mishuserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.upc.mishuserver.model.StringSetting;

/**
 * StringSettingRepositoriy
 */
public interface StringSettingRepositoriy extends JpaRepository<StringSetting,Long>{
    StringSetting findByItem(String item);
}