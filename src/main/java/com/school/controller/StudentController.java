package com.school.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.dto.StudentDTO;
import com.school.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

	@Autowired
	private StudentService studentService;
 
	@PostMapping("/saveStudent")
	@Operation(summary = "Saves Single Student", description = "Save a new Student")
	public StudentDTO saveStudent(@RequestBody @Valid StudentDTO studentDto) {
		return studentService.saveStudent(studentDto);
	}

	@GetMapping("/getAllStudent")
	public List<StudentDTO> getAllStudent() {
		System.out.println("getAllStudent started");
		return studentService.getAllStudent();
	}

	@GetMapping("/getSingleStudent/{id}")
	public ResponseEntity<?> getSingleStudent(@PathVariable Integer id) {
		StudentDTO student = studentService.getSingleStudent(id);
		return new ResponseEntity<StudentDTO>(student, HttpStatus.OK);

	}

	@DeleteMapping("/deleteStudentById/{id}")
	public ResponseEntity<String> deleteStudentById(@PathVariable int id) {
		studentService.deleteStudentById(id);
		return new ResponseEntity<String>("Record Deleted of Student id" + id, HttpStatus.OK);

	}

	@PutMapping("/updateStudentById/{id}")
	public ResponseEntity<StudentDTO> updateStudentById(@PathVariable int id, @RequestBody StudentDTO studentDTO) {
		return new ResponseEntity<StudentDTO>(studentService.updateStudentById(id, studentDTO), HttpStatus.OK);

	}

	@PatchMapping("updateStudentPhoto/{studId}")
	public ResponseEntity<StudentDTO> updateStudentPhoto(@PathVariable Integer studId,
			@RequestBody Map<String, String> request) {
		return ResponseEntity.ok(studentService.updateStudentPhoto(studId, request));
	}

}
