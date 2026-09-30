package com.placement.repository;

import com.placement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // With shared-PK (@MapsId), student.id == user.id, so findById(userId) works directly.
    // These derived queries navigate via the joined User.
    Optional<Student> findByUserEmail(String email);
    Optional<Student> findByEnrollmentNumber(String enrollmentNumber);
}
