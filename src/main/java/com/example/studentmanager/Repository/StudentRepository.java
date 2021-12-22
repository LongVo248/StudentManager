package com.example.studentmanager.Repository;

import com.example.studentmanager.Student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Page<Student> findByNameContaining(String name, Pageable pageable);

    @Query("SELECT p FROM Student p WHERE CONCAT(p.name, ' ', p.email, ' ', p.address, ' ', p.phone) LIKE %?1%")
    public List<Student> search(String keyword);
}
