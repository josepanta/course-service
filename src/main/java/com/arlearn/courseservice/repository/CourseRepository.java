package com.arlearn.courseservice.repository;

import com.arlearn.courseservice.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    public Optional<Course> findById(Integer id);
}