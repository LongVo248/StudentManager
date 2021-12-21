package com.example.studentmanager.Service;

import com.example.studentmanager.Exception.NoDataFoundException;
import com.example.studentmanager.Exception.StudentAlreadyExistedException;
import com.example.studentmanager.Exception.StudentNotFoundException;
import com.example.studentmanager.Repository.StudentRepository;
import com.example.studentmanager.Student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements IStudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> findAll() {
        var students = (List<Student>) studentRepository.findAll();
        if (students.isEmpty()) {
            throw new NoDataFoundException();
        }

        return students;
    }

    @Override
    public Optional<Student> findById(Integer id) {
        var student = studentRepository.findById(id);
        if (student.isPresent()) {
            return student;
        } else {
            throw new StudentNotFoundException(id);
        }
//        return studentRepository.findById(id)
//                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public Student save(Student student) throws StudentAlreadyExistedException {
        var studentList = (List<Student>) studentRepository.findAll();

        if (checkTrung(studentList, student.getEmail())) {
            throw new StudentAlreadyExistedException(student.getEmail());
        } else
            return studentRepository.save(student);
    }

    @Override
    public void remove(Integer id) {
        studentRepository.deleteById(id);
    }

    public boolean checkTrung(List<Student> students, String email) {
        for (Student s : students) {
            if (s.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}
