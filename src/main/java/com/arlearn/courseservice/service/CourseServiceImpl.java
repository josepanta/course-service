package com.arlearn.courseservice.service;

import com.arlearn.courseservice.entity.Course;
import com.arlearn.courseservice.model.CourseRequest;
import com.arlearn.courseservice.repository.CourseRepository;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService{

    private static final char ABLE = 'A';
    private static final char DISABLE = 'I';

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Maybe<List<Course>> getAll() {
        return Maybe.fromCallable(() -> Optional.of(courseRepository.findAll()).orElseThrow(Exception::new));
    }

    @Override
    public Single<Course> getById(Integer id) {
        return Single.fromCallable(() -> courseRepository.findById(id).orElseThrow(Exception::new));
    }

    @Override
    public Completable update(Integer id, CourseRequest courseRequest) {
        return Completable.fromCallable(() -> courseRepository.findById(id).map(course -> {
            course.setName(courseRequest.getName());
            course.setDescription(courseRequest.getDescription());
            if (courseRequest.getState() != null) course.setState(courseRequest.getState().charAt(0));
            return courseRepository.save(course);
        }).orElseThrow(Exception::new));
    }

    @Override
    public Completable save(CourseRequest courseRequest) {
        return Completable.fromCallable(() -> Optional.of(courseRepository.save(Course.builder()
                .name(courseRequest.getName())
                .description(courseRequest.getDescription())
                .state(ABLE)
                .build()))
                .orElseThrow(Exception::new));
    }

    @Override
    public Completable disabled(Integer id) {
        return Completable.fromCallable(() -> courseRepository.findById(id).map(course -> {
            course.setState(DISABLE);
            return courseRepository.save(course);
        }).orElseThrow(Exception::new));
    }
}
