package com.example.studentmanager.Repository;

import com.example.studentmanager.Student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    //List<Student> findByPublished(boolean published);

    List<Student> findByNameContaining(String name);
}
