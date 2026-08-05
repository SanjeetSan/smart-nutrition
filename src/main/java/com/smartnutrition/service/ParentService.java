package com.smartnutrition.service;

import com.smartnutrition.dto.request.LinkStudentRequest;
import com.smartnutrition.dto.response.StudentResponse;
import com.smartnutrition.entity.ParentStudent;
import com.smartnutrition.entity.Student;
import com.smartnutrition.entity.User;
import com.smartnutrition.repository.ParentStudentRepository;
import com.smartnutrition.repository.StudentRepository;
import com.smartnutrition.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ParentStudentRepository parentStudentRepository;

    public ParentService(UserRepository userRepository,
                         StudentRepository studentRepository,
                         ParentStudentRepository parentStudentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.parentStudentRepository = parentStudentRepository;
    }

    @Transactional
    public StudentResponse linkStudent(String parentEmail, LinkStudentRequest request) {
        User parent = userRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Parent user not found"));

        Student student = studentRepository.findByStudentCode(request.studentCode())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with code: " + request.studentCode()));

        if (parentStudentRepository.existsByParentAndStudent(parent, student)) {
            throw new IllegalArgumentException("Student is already linked to your account");
        }

        ParentStudent parentStudent = ParentStudent.builder()
                .parent(parent)
                .student(student)
                .relationship(request.relationship())
                .isPrimary(request.isPrimary() != null ? request.isPrimary() : true)
                .build();

        parentStudentRepository.save(parentStudent);

        return mapToStudentResponse(student);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getLinkedStudents(String parentEmail) {
        User parent = userRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Parent user not found"));

        List<ParentStudent> links = parentStudentRepository.findByParentId(parent.getId());
        return links.stream()
                .map(link -> mapToStudentResponse(link.getStudent()))
                .toList();
    }

    private StudentResponse mapToStudentResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getRollNumber(),
                student.getStudentCode(),
                student.getGender(),
                student.getDateOfBirth(),
                student.getWeightKg(),
                student.getHeightCm(),
                student.getBloodGroup(),
                student.getStudentClass() != null ? student.getStudentClass().getClassName() + " " + student.getStudentClass().getSection() : "N/A",
                student.getSchool() != null ? student.getSchool().getName() : "N/A"
        );
    }
}
