package com.smartnutrition.controller;

import com.smartnutrition.dto.request.LinkStudentRequest;
import com.smartnutrition.dto.response.StudentResponse;
import com.smartnutrition.service.ParentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parent")
@Tag(name = "Parent", description = "Parent student linking and management endpoints")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @PostMapping("/link-student")
    @PreAuthorize("hasRole('PARENT')")
    @Operation(summary = "Link parent account to a student using student code (e.g. STU-A001)")
    public ResponseEntity<StudentResponse> linkStudent(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody LinkStudentRequest request) {
        StudentResponse response = parentService.linkStudent(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/students")
    @PreAuthorize("hasRole('PARENT')")
    @Operation(summary = "Get all students linked to the authenticated parent")
    public ResponseEntity<List<StudentResponse>> getLinkedStudents(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<StudentResponse> students = parentService.getLinkedStudents(userDetails.getUsername());
        return ResponseEntity.ok(students);
    }
}
