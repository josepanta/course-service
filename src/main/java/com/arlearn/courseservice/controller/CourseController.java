package com.arlearn.courseservice.controller;

import com.arlearn.courseservice.api.CourseApi;
import com.arlearn.courseservice.entity.Course;
import com.arlearn.courseservice.model.CourseRequest;
import com.arlearn.courseservice.model.CourseResponse;
import com.arlearn.courseservice.service.CourseService;
import io.reactivex.Maybe;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CourseController implements CourseApi {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    public void init(){
        configMapperCourseResponseFromCourse();
    }

    @Override
    public Maybe<ResponseEntity<List<CourseResponse>>> getAll() {
        return courseService.getAll()
                .map(courses -> ResponseEntity.status(HttpStatus.OK)
                        .body(courses.stream()
                                .map(this::convertCourseToCourseResponse)
                                .collect(Collectors.toList())))
                .onErrorResumeNext(this::buildEmptyListCourseResponseError);
    }

    @Override
    public Maybe<ResponseEntity<CourseResponse>> getById(Long id) {
        return courseService.getById(id.intValue())
                .map(course -> ResponseEntity.status(HttpStatus.OK)
                        .body(convertCourseToCourseResponse(course)))
                .toMaybe()
                .onErrorResumeNext(this::buildEmptyCourseResponseError);
    }

    @Override
    public Maybe<ResponseEntity<Void>> save(CourseRequest courseRequest) {
        return courseService.save(courseRequest)
                .toSingle(() -> ResponseEntity.ok().<Void>build())
                .toMaybe()
                .onErrorResumeNext(this::buildVoidError);
    }

    @Override
    public Maybe<ResponseEntity<Void>> update(Long id, CourseRequest courseRequest) {
        return courseService.update(id.intValue(), courseRequest)
                .toSingle(() -> ResponseEntity.ok().<Void>build())
                .toMaybe()
                .onErrorResumeNext(this::buildVoidError);
    }

    @Override
    public Maybe<ResponseEntity<Void>> disable(Long id) {
        return courseService.disabled(id.intValue())
                .toSingle(() -> ResponseEntity.ok().<Void>build())
                .toMaybe()
                .onErrorResumeNext(this::buildVoidError);
    }

    private Maybe<ResponseEntity<CourseResponse>> buildEmptyCourseResponseError (Throwable throwable){
        return Maybe.just(ResponseEntity.noContent().<CourseResponse>build());
    }

    private Maybe<ResponseEntity<List<CourseResponse>>> buildEmptyListCourseResponseError (Throwable throwable){
        return Maybe.just(ResponseEntity.noContent().<List<CourseResponse>>build());
    }

    private Maybe<ResponseEntity<Void>> buildVoidError (Throwable throwable){
        return Maybe.just(ResponseEntity.noContent().<Void>build());
    }

    private CourseResponse convertCourseToCourseResponse(Course course){
        return modelMapper.map(course, CourseResponse.class);
    }

    private void configMapperCourseResponseFromCourse(){
        TypeMap<Course, CourseResponse> propertyMapper = modelMapper.createTypeMap(Course.class, CourseResponse.class);
        propertyMapper.addMapping(Course::getId, CourseResponse::setId);
    }
}