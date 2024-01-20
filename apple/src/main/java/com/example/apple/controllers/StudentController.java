package com.example.apple.controllers;

import com.example.apple.exception.ResourceNotFoundException;
import com.example.apple.models.StudentModel;
import com.example.apple.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin({"*"})
@RestController

public class StudentController {

    @Autowired
    private StudentRepository StudentRepo;

    public StudentController(){
    }

    @GetMapping
    public List<StudentModel> getAllStudents() {
        return this.StudentRepo.findAll();
    }

    @PostMapping
    public StudentModel createStudent(@RequestBody StudentModel student) {
        return (StudentModel) this.StudentRepo.save(student);
    }

    @GetMapping({"{id}"})
    public ResponseEntity<StudentModel> getStudentById(@PathVariable long id) {
        StudentModel student = (StudentModel)this.StudentRepo.findById(id).orElseThrow(() -> {
            return new ResourceNotFoundException("Employee not exist with id:" + id);
        });
        return ResponseEntity.ok(student);
    }

    @PutMapping({"{id}"})
    public ResponseEntity<StudentModel> updateStudent(@PathVariable long id, @RequestBody StudentModel student) {
        StudentModel updateStudent = (StudentModel)this.StudentRepo.findById(id).orElseThrow(() -> {
            return new ResourceNotFoundException("Student with id: " + id + " does not exist");
        });
        updateStudent.setSurname(student.getSurname());
        updateStudent.setName(student.getName());
        updateStudent.setFatherName(student.getFatherName());
        updateStudent.setEmail(student.getEmail());
        updateStudent.setPhone(student.getPhone());
        this.StudentRepo.save(updateStudent);
        return ResponseEntity.ok(updateStudent);
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable long id) {
        StudentModel student = (StudentModel)this.StudentRepo.findById(id).orElseThrow(() -> {
            return new ResourceNotFoundException("Student with id: " + id + " does not exist");
        });
        this.StudentRepo.delete(student);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}