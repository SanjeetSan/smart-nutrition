-- Seed School
INSERT INTO schools (id, name, academic_year, address, is_active, created_at)
VALUES (1, 'Greenwood International School', '2025-2026', '123 Education Lane, Tech City', true, CURRENT_TIMESTAMP());

-- Seed Classes
INSERT INTO classes (id, school_id, class_name, section, academic_year, is_active)
VALUES (1, 1, 'Grade 3', 'A', '2025-2026', true);

INSERT INTO classes (id, school_id, class_name, section, academic_year, is_active)
VALUES (2, 1, 'Grade 4', 'B', '2025-2026', true);

-- Seed Students
INSERT INTO students (id, school_id, class_id, name, roll_number, student_code, gender, date_of_birth, weight_kg, height_cm, blood_group, is_active)
VALUES (1, 1, 1, 'Arjun Sharma', '01', 'STU-A001', 'MALE', '2016-05-14', 28.50, 132.00, 'O+', true);

INSERT INTO students (id, school_id, class_id, name, roll_number, student_code, gender, date_of_birth, weight_kg, height_cm, blood_group, is_active)
VALUES (2, 1, 1, 'Ananya Patel', '02', 'STU-A002', 'FEMALE', '2016-08-22', 26.00, 128.50, 'A+', true);
