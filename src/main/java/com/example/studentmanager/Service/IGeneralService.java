package com.example.studentmanager.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<T> {
    List<T> findAll();

    Optional<T> findById(Integer id);

    T save(T t);

    void remove(Integer id);

//    Page<T> findByNameContaining(String name, Pageable pageable);
}
