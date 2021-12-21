package com.example.studentmanager.Repository;

import com.example.studentmanager.Student.Student;
import com.example.studentmanager.Student.StudentPage;
import com.example.studentmanager.Student.StudentSearchCriteria;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class StudentCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public StudentCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Student> findAllWithFilters(StudentPage studentPage,
                                             StudentSearchCriteria studentSearchCriteria){
        CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
        Root<Student> studentRoot = criteriaQuery.from(Student.class);
        Predicate predicate = getPredicate(studentSearchCriteria, studentRoot);
        criteriaQuery.where(predicate);
        setOrder(studentPage, criteriaQuery, studentRoot);

        TypedQuery<Student> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(studentPage.getPageNumber() * studentPage.getPageSize());
        typedQuery.setMaxResults(studentPage.getPageSize());

        Pageable pageable = getPageable(studentPage);

        long studentCount = getStudentCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, studentCount);
    }

    private Predicate getPredicate(StudentSearchCriteria studentSearchCriteria,
                                   Root<Student> studentRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(studentSearchCriteria.getName())) {
            predicates.add(
                    criteriaBuilder.like(studentRoot.get("name"),
                            "%" + studentSearchCriteria.getName() + "%")
            );
        }
        if (Objects.nonNull(studentSearchCriteria.getEmail())) {
            predicates.add(
                    criteriaBuilder.like(studentRoot.get("email"),
                            "%" + studentSearchCriteria.getEmail() + "%")
            );
        }
        if (Objects.nonNull(studentSearchCriteria.getAddress())) {
            predicates.add(
                    criteriaBuilder.like(studentRoot.get("address"),
                            "%" + studentSearchCriteria.getAddress() + "%")
            );
        }
        if (Objects.nonNull(studentSearchCriteria.getDob())) {
            predicates.add(
                    criteriaBuilder.like(studentRoot.get("dob"),
                            "%" + studentSearchCriteria.getDob() + "%")
            );
        }
        if (Objects.nonNull(studentSearchCriteria.getPhone())) {
            predicates.add(
                    criteriaBuilder.like(studentRoot.get("phone"),
                            "%" + studentSearchCriteria.getPhone() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(StudentPage studentPage, CriteriaQuery<Student> criteriaQuery, Root<Student> studentRoot) {
        if (studentPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(studentRoot.get(studentPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(studentRoot.get(studentPage.getSortBy())));
        }
    }

    private Pageable getPageable(StudentPage studentPage) {
        Sort sort= Sort.by(studentPage.getSortDirection(), studentPage.getSortBy());
        return PageRequest.of(studentPage.getPageNumber(),studentPage.getPageSize(), sort);
    }


    private long getStudentCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery= criteriaBuilder.createQuery(Long.class);
        Root<Student> countRoot= countQuery.from(Student.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
