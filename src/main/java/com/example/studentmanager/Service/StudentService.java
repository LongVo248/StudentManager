package com.example.studentmanager.Service;

import com.example.studentmanager.Exception.NoDataFoundException;
import com.example.studentmanager.Exception.StudentAlreadyExistedException;
import com.example.studentmanager.Exception.StudentNotFoundException;
import com.example.studentmanager.Repository.StudentCriteriaRepository;
import com.example.studentmanager.Repository.StudentPageRepository;
import com.example.studentmanager.Repository.StudentRepository;
import com.example.studentmanager.Student.Student;
import com.example.studentmanager.Student.StudentPage;
import com.example.studentmanager.Student.StudentSearchCriteria;
import com.example.studentmanager.Student.utils.PagingHeaders;
import com.example.studentmanager.Student.utils.PagingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class StudentService implements IStudentService {
    //@Autowired
    private final StudentRepository studentRepository;

    @Autowired
    private StudentPageRepository studentPageRepository;
    //@Autowired
    private final StudentCriteriaRepository studentCriteriaRepository;

    public StudentService(StudentRepository studentRepository, StudentCriteriaRepository studentCriteriaRepository) {
        this.studentRepository = studentRepository;
        this.studentCriteriaRepository = studentCriteriaRepository;
    }

    public Page<Student> getStudents(StudentPage studentPage, StudentSearchCriteria studentSearchCriteria) {
        return studentCriteriaRepository.findAllWithFilters(studentPage, studentSearchCriteria);
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

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

    public List<Student> listAll(String keyword) {
        if (keyword != null) {
            return studentRepository.search(keyword);
        }
        return studentRepository.findAll();
    }

    public PagingResponse get(Specification<Student> spec, HttpHeaders headers, Sort sort) {
        if (isRequestPaged(headers)) {
            return get(spec, buildPageRequest(headers, sort));
        } else {
            final List<Student> entities = get(spec, sort);
            return new PagingResponse((long) entities.size(), 0L, 0L, 0L, 0L, entities);
        }
    }
    public PagingResponse get(Specification<Student> spec, Pageable pageable) {
        Page<Student> page = studentPageRepository.findAll(spec, pageable);
        List<Student> content = page.getContent();
        return new PagingResponse(page.getTotalElements(), (long) page.getNumber(), (long) page.getNumberOfElements(), pageable.getOffset(), (long) page.getTotalPages(), content);
    }
    private boolean isRequestPaged(HttpHeaders headers) {
        return headers.containsKey(PagingHeaders.PAGE_NUMBER.getName()) && headers.containsKey(PagingHeaders.PAGE_SIZE.getName());
    }

    private Pageable buildPageRequest(HttpHeaders headers, Sort sort) {
        int page = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_NUMBER.getName())).get(0));
        int size = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_SIZE.getName())).get(0));
        return PageRequest.of(page, size, sort);
    }

    public List<Student> get(Specification<Student> spec, Sort sort) {
        return studentPageRepository.findAll(spec, sort);
    }
}
