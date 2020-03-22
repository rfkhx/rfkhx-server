package edu.upc.mishuserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.upc.mishuserver.dto.AppBinary;

/**
 * AppBinaryRepository
 */
public interface AppBinaryRepository extends JpaRepository<AppBinary,Long> {

    AppBinary findFirstByPlatformOrderByVersioncode(String platform);
}