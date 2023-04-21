package com.arlearn.courseservice.service;

import com.arlearn.courseservice.entity.Course;
import com.arlearn.courseservice.model.CourseRequest;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

public interface CourseService {

    public Maybe<List<Course>> getAll();

    public Single<Course> getById(Integer id);

    public Completable update(Integer id, CourseRequest courseRequest);

    public Completable save(CourseRequest courseRequest);

    public Completable disabled(Integer id);

}
