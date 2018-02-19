package com.example.resume2;

import org.springframework.data.repository.CrudRepository;

import javax.persistence.Id;

public interface ResumeRepository extends CrudRepository<Resume,Long> {
    Resume findTopByOrderByIdDesc();

    @Override
    Iterable<Resume> findAll();
}
