package com.school.service;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.dto.StudentDTO;
import com.school.exception.StudentCustomException;
import com.school.model.Student;
import com.school.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ModelMapper modelMapper;

	public List<StudentDTO> getAllStudent() {
		List<Student> studentList = studentRepository.findAll();
		return studentList.stream().map(stud -> modelMapper.map(stud, StudentDTO.class)).toList();
	}

	public StudentDTO getSingleStudent(Integer id) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new StudentCustomException("Student not found"));
		return modelMapper.map(student, StudentDTO.class);
	}

	public Student getStudentById(Integer id) {
		return studentRepository.findById(id).orElseThrow(() -> new StudentCustomException("Student not found"));
	}

	public StudentDTO saveStudent(StudentDTO studentDTO) {
		Student student = modelMapper.map(studentDTO, Student.class);
		Student studentDB = studentRepository.save(student);
		return modelMapper.map(studentDB, StudentDTO.class);
	}

	public void deleteStudentById(int id) {
		if (studentRepository.existsById(id)) {
			studentRepository.deleteById(id);
		} else {
			throw new StudentCustomException("Student with id " + id + " does not exists");
		}

	}

	public StudentDTO updateStudentById(int id, StudentDTO newStud) {
		if (!studentRepository.existsById(id))
			throw new StudentCustomException("Student with id " + id + " does not exists");
		Student s1 = getStudentById(id);
		s1.setStudName(newStud.getStudName());
		s1.setStudEmail(newStud.getStudEmail());
		s1.setStudCourse(newStud.getStudCourse());
		s1.setStudPhoto(newStud.getStudPhoto());
		Student updatedStudent = studentRepository.save(s1);
		return modelMapper.map(updatedStudent, StudentDTO.class);

	}

	public StudentDTO updateStudentPhoto(Integer studId, Map<String, String> request) {
		if (!request.containsKey("studPhoto")) {
			throw new StudentCustomException("Invalid Request, upload valid Profile Picture");
		}
		if (!studentRepository.existsById(studId))
			throw new StudentCustomException("Student with id " + studId + " does not exists");
		Student s1 = getStudentById(studId);
		s1.setStudPhoto(request.get("studPhoto"));
		Student updatedStudent = studentRepository.save(s1);
		return modelMapper.map(updatedStudent, StudentDTO.class);
	}

}
