package com.smartnutrition.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class_ studentClass;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "roll_number", length = 20)
    private String rollNumber;

    @Column(name = "student_code", unique = true, nullable = false, length = 20)
    private String studentCode;

    @Column(length = 10)
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "weight_kg", precision = 5, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "height_cm", precision = 5, scale = 2)
    private BigDecimal heightCm;

    @Column(name = "blood_group", length = 5)
    private String bloodGroup;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Student() {}

    public Long getId() { return id; }
    public School getSchool() { return school; }
    public Class_ getStudentClass() { return studentClass; }
    public String getName() { return name; }
    public String getRollNumber() { return rollNumber; }
    public String getStudentCode() { return studentCode; }
    public String getGender() { return gender; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public BigDecimal getWeightKg() { return weightKg; }
    public BigDecimal getHeightCm() { return heightCm; }
    public String getBloodGroup() { return bloodGroup; }
    public Boolean getIsActive() { return isActive; }

    public void setSchool(School school) { this.school = school; }
    public void setStudentClass(Class_ studentClass) { this.studentClass = studentClass; }
    public void setName(String name) { this.name = name; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }
    public void setHeightCm(BigDecimal heightCm) { this.heightCm = heightCm; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Student s = new Student();
        public Builder school(School v) { s.school = v; return this; }
        public Builder studentClass(Class_ v) { s.studentClass = v; return this; }
        public Builder name(String v) { s.name = v; return this; }
        public Builder rollNumber(String v) { s.rollNumber = v; return this; }
        public Builder studentCode(String v) { s.studentCode = v; return this; }
        public Builder gender(String v) { s.gender = v; return this; }
        public Builder dateOfBirth(LocalDate v) { s.dateOfBirth = v; return this; }
        public Builder weightKg(BigDecimal v) { s.weightKg = v; return this; }
        public Builder heightCm(BigDecimal v) { s.heightCm = v; return this; }
        public Builder bloodGroup(String v) { s.bloodGroup = v; return this; }
        public Builder isActive(Boolean v) { s.isActive = v; return this; }
        public Student build() { return s; }
    }
}
