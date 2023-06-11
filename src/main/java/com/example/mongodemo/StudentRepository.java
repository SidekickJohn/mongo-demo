package com.example.mongodemo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

// Second in generic is datatype of id, here: String
public interface StudentRepository extends MongoRepository<Student, String> {

    Optional<Student> findStudentByEmail(String email);
}
