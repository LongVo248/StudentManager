package com.example.studentmanager.Exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Integer id) {
        super(String.valueOf(id));
    }
}
