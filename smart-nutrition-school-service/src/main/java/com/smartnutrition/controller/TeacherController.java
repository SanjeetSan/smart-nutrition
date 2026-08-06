package com.smartnutrition.controller;

import com.smartnutrition.dto.response.ClassResponse;
import com.smartnutrition.dto.response.StudentResponse;
import com.smartnutrition.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@Tag(name = "Teacher Operations", description = "Teacher class overview and student roster management")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/classes")
    @Operation(summary = "Get teacher assigned classes", description = "Retrieves active classes managed by or accessible to teachers.")
    public ResponseEntity<List<ClassResponse>> getTeacherClasses() {
        return ResponseEntity.ok(teacherService.getTeacherClasses());
    }

    @GetMapping("/students")
    @Operation(summary = "Get students in a class", description = "Retrieves student roster for a specific class code.")
    public ResponseEntity<List<StudentResponse>> getStudentsByClassCode(
            @Parameter(description = "The class code (e.g. CLS-3A)", required = true)
            @RequestParam("classCode") String classCode) {
        return ResponseEntity.ok(teacherService.getStudentsByClassCode(classCode));
    }
}
