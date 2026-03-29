package com.group3.cityroad.repository;

import com.group3.cityroad.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByAvailable(Boolean available);
    
    @Query(value = "SELECT * FROM resource r WHERE type = :resourceType", nativeQuery = true)
    List<Resource> findByResourceTypeNative(@Param("resourceType") String resourceType);
}
