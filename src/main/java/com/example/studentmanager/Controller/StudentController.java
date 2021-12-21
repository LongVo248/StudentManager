package com.example.studentmanager.Controller;

import com.example.studentmanager.Repository.StudentRepository;
import com.example.studentmanager.Service.StudentService;
import com.example.studentmanager.Student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> createNewStudent(@Valid @RequestBody Student student) {
        return new ResponseEntity<>(studentService.save(student), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<Student>> getAllStudent() {
        return new ResponseEntity<>(studentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@Valid @PathVariable Integer id){
        Optional<Student> studentOptional= studentService.findById(id);
        return studentOptional.map(student -> new ResponseEntity<>(student, HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@Valid @PathVariable Integer id,@Valid @RequestBody Student student) {
        Optional<Student> studentOptional = studentService.findById(id);
        return studentOptional.map(student1 -> {
            student.setId(student1.getId());
            return new ResponseEntity<>(studentService.save(student), HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteStudent(@Valid @PathVariable Integer id) {
       try {
           studentService.remove(id);
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       } catch (Exception e){
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Student> deleteCategory(@PathVariable Integer id) {
//        Optional<Student> studentOptional = studentService.findById(id);
//        return studentOptional.map(student -> {
//            studentService.remove(id);
//            return new ResponseEntity<>(student, HttpStatus.OK);
//        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }

//    @DeleteMapping
//    public ResponseEntity<HttpStatus> deleteAllStudent(){
//
//    }
//    @Autowired
//    StudentRepository studentRepository;
//
//    @GetMapping("/students")
//    public ResponseEntity<List<Student>> getAllStudents(@RequestParam(required = false) String name) {
//        try {
//            List<Student> students = new ArrayList<Student>();
//            if (name == null) {
//                studentRepository.findAll().forEach(students::add);
//            } else {
//                studentRepository.findByNameContaining(name).forEach(students::add);
//            }
//            if (students.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<>(students, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @GetMapping("/students/{id}")
//    public ResponseEntity<Student> getStudentById(@PathVariable(value = "id") int id) {
//        Optional<Student> student = studentRepository.findById(id);
//
//        if (student.isPresent()) {
//            return new ResponseEntity<>(student.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
////    @GetMapping("/students/{id}")
////    public Student getStudent(@PathVariable("id") int id) {
////        return studentRepository.findById(id).get();
////    }
//
//    @PostMapping("/students")
//    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
//        try {
//            Student _student = studentRepository.save(new Student(student.getId(), student.getName(), student.getEmail(), student.getAddress(), student.getDob()));
//            return new ResponseEntity<>(_student, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PutMapping("/students/{id}")
//    public ResponseEntity<Student> updateStudent(@PathVariable("id") int id, @RequestBody Student student) {
//        Optional<Student> studentData = studentRepository.findById(id);
//
//        if (studentData.isPresent()) {
//            Student _student = studentData.get();
//            _student.setName(student.getName());
//            _student.setEmail(student.getEmail());
//            _student.setAddress(student.getAddress());
//            _student.setDob(student.getDob());
//            return new ResponseEntity<>(studentRepository.save(_student), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @DeleteMapping("/students/{id}")
//    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable("id") int id) {
//        try {
//            studentRepository.deleteById(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @DeleteMapping("/students")
//    public ResponseEntity<HttpStatus> deleteAllStudents() {
//        try {
//            studentRepository.deleteAll();
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }

}
