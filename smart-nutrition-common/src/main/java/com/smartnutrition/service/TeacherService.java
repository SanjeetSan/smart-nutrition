package com.smartnutrition.service;

import com.smartnutrition.dto.response.ClassResponse;
import com.smartnutrition.dto.response.StudentResponse;
import com.smartnutrition.entity.Class_;
import com.smartnutrition.repository.ClassRepository;
import com.smartnutrition.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherService {

    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;

    public TeacherService(ClassRepository classRepository, StudentRepository studentRepository) {
        this.classRepository = classRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<ClassResponse> getTeacherClasses() {
        return classRepository.findByIsActiveTrue().stream()
                .map(c -> new ClassResponse(
                        c.getId(),
                        c.getClassName(),
                        c.getSection(),
                        c.getAcademicYear(),
                        c.getClassCode(),
                        c.getSchool() != null ? c.getSchool().getName() : "N/A"
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getStudentsByClassCode(String classCode) {
        Class_ studentClass = classRepository.findByClassCodeIgnoreCase(classCode.trim())
                .orElseThrow(() -> new IllegalArgumentException("Class not found with code: " + classCode));

        return studentRepository.findByStudentClassId(studentClass.getId()).stream()
                .map(s -> new StudentResponse(
                        s.getId(),
                        s.getName(),
                        s.getRollNumber(),
                        s.getStudentCode(),
                        s.getGender(),
                        s.getDateOfBirth(),
                        s.getWeightKg(),
                        s.getHeightCm(),
                        s.getBloodGroup(),
                        s.getStudentClass() != null ? s.getStudentClass().getClassName() + " " + s.getStudentClass().getSection() : "N/A",
                        s.getSchool() != null ? s.getSchool().getName() : "N/A"
                ))
                .toList();
    }
}
