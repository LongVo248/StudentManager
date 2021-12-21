package com.example.studentmanager.Repository;

import com.example.studentmanager.Student.Student;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StudentPageRepository extends PagingAndSortingRepository<Student, Integer> {
}
