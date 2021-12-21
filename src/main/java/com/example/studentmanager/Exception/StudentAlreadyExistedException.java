package com.example.studentmanager.Exception;

public class StudentAlreadyExistedException extends RuntimeException {
    public StudentAlreadyExistedException(String email) {
        super(email);
    }
}
