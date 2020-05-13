package edu.upc.mishuserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.upc.mishuserver.model.AppBinary;

/**
 * AppBinaryRepository
 */
public interface AppBinaryRepository extends JpaRepository<AppBinary,Long> {

    AppBinary findFirstByPlatformOrderByVersioncodeDesc(String platform);
}