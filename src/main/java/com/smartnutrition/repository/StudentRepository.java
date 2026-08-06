package com.smartnutrition.repository;

import com.smartnutrition.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentCode(String studentCode);

    boolean existsByStudentCode(String studentCode);

    java.util.List<Student> findByStudentClassId(Long classId);

    java.util.List<Student> findByStudentClassClassCode(String classCode);
}
