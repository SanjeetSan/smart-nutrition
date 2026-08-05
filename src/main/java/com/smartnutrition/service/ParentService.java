package com.smartnutrition.service;

import com.smartnutrition.dto.request.CreateStudentRequest;
import com.smartnutrition.dto.request.LinkStudentRequest;
import com.smartnutrition.dto.response.ClassResponse;
import com.smartnutrition.dto.response.StudentResponse;
import com.smartnutrition.entity.Class_;
import com.smartnutrition.entity.ParentStudent;
import com.smartnutrition.entity.Student;
import com.smartnutrition.entity.User;
import com.smartnutrition.repository.ClassRepository;
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
    private final ClassRepository classRepository;

    public ParentService(UserRepository userRepository,
                         StudentRepository studentRepository,
                         ParentStudentRepository parentStudentRepository,
                         ClassRepository classRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.parentStudentRepository = parentStudentRepository;
        this.classRepository = classRepository;
    }

    @Transactional
    public StudentResponse createAndLinkStudent(String parentEmail, CreateStudentRequest request) {
        User parent = userRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Parent user not found"));

        Class_ studentClass = classRepository.findByClassCode(request.classCode())
                .orElseThrow(() -> new IllegalArgumentException("Class not found with code: " + request.classCode()));

        // Generate unique student code (e.g. STU-123456)
        String studentCode;
        do {
            studentCode = "STU-" + String.format("%06d", (int)(Math.random() * 1000000));
        } while (studentRepository.findByStudentCode(studentCode).isPresent());

        Student student = Student.builder()
                .name(request.name())
                .rollNumber(request.rollNumber())
                .studentCode(studentCode)
                .gender(request.gender())
                .dateOfBirth(request.dateOfBirth())
                .weightKg(request.weightKg())
                .heightCm(request.heightCm())
                .bloodGroup(request.bloodGroup())
                .studentClass(studentClass)
                .school(studentClass.getSchool())
                .isActive(true)
                .build();

        student = studentRepository.save(student);

        ParentStudent parentStudent = ParentStudent.builder()
                .parent(parent)
                .student(student)
                .relationship(request.relationship())
                .isPrimary(true)
                .build();

        parentStudentRepository.save(parentStudent);

        return mapToStudentResponse(student);
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

    @Transactional(readOnly = true)
    public List<ClassResponse> getActiveClasses() {
        return classRepository.findByIsActiveTrue().stream()
                .map(c -> new ClassResponse(
                        c.getId(),
                        c.getClassName(),
                        c.getSection(),
                        c.getAcademicYear(),
                        c.getSchool() != null ? c.getSchool().getName() : "N/A"
                ))
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
