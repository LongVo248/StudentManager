package com.example.studentmanager.Controller;

import com.example.studentmanager.Repository.StudentPageRepository;
import com.example.studentmanager.Repository.StudentRepository;
import com.example.studentmanager.Service.StudentService;
import com.example.studentmanager.Student.Student;
import com.example.studentmanager.Student.StudentPage;
import com.example.studentmanager.Student.StudentSearchCriteria;
import com.example.studentmanager.Student.utils.PagingHeaders;
import com.example.studentmanager.Student.utils.PagingResponse;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class StudentController {


    @Autowired
    private final StudentService studentService;

    @Autowired
    private StudentPageRepository studentPageRepository;

    @Autowired
    private StudentRepository studentRepository;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Value("${spring.messages}")
    private String message;

    @GetMapping("/rest/hello")
    public String sayHello() {
        return message;
    }

    @GetMapping("/page")
    public Page<Student> getStudents(@RequestParam Optional<Integer> page, @RequestParam Optional<String> sortBy) {
        return studentRepository.findAll(
                PageRequest.of(
                        page.orElse(0),
                        5,
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @Transactional
    @GetMapping(value = "/mapsearch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Student>> get(
            @And({
                    @Spec(path = "name", params = "name", spec = Like.class),
                    @Spec(path = "email", params = "email", spec = Like.class),
                    @Spec(path = "address", params = "address", spec = In.class),
                    @Spec(path = "phone", params = "phone", spec = Like.class),
//                    @Spec(path = "createDate", params = "createDate", spec = Equal.class),
//                    @Spec(path = "createDate", params = {"createDateGt", "createDateLt"}, spec = Between.class)
            }) Specification<Student> spec,
            Sort sort,
            @RequestHeader HttpHeaders headers) {
        final PagingResponse response = studentService.get(spec, headers, sort);
        return new ResponseEntity<>(response.getElements(), returnHttpHeaders(response), HttpStatus.OK);
    }

    public HttpHeaders returnHttpHeaders(PagingResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(PagingHeaders.COUNT.getName(), String.valueOf(response.getCount()));
        headers.set(PagingHeaders.PAGE_SIZE.getName(), String.valueOf(response.getPageSize()));
        headers.set(PagingHeaders.PAGE_OFFSET.getName(), String.valueOf(response.getPageOffset()));
        headers.set(PagingHeaders.PAGE_NUMBER.getName(), String.valueOf(response.getPageNumber()));
        headers.set(PagingHeaders.PAGE_TOTAL.getName(), String.valueOf(response.getPageTotal()));
        return headers;
    }

//    @GetMapping("/page/get")
//    public ResponseEntity<Page<Student>> getStudents (StudentPage studentPage, StudentSearchCriteria studentSearchCriteria){
//        return new ResponseEntity<>(studentService.getStudents(studentPage,studentSearchCriteria), HttpStatus.OK);
//    }
//
//    @PostMapping("/page/add")
//    public ResponseEntity<Student> addStudent(@RequestBody Student student){
//        return new ResponseEntity<>(studentService.addStudent(student), HttpStatus.OK);
//    }


    @PostMapping
    public ResponseEntity<Student> createNewStudent(@Valid @RequestBody Student student) {
        return new ResponseEntity<>(studentService.save(student), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudent() {
        return new ResponseEntity<>(studentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@Valid @PathVariable Integer id) {
        Optional<Student> studentOptional = studentService.findById(id);
        return studentOptional.map(student -> new ResponseEntity<>(student, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@Valid @PathVariable Integer id, @Valid @RequestBody Student student) {
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
        } catch (Exception e) {
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
