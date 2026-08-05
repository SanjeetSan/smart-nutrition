package com.smartnutrition.repository;

import com.smartnutrition.entity.Class_;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class_, Long> {
    List<Class_> findByIsActiveTrue();
}
