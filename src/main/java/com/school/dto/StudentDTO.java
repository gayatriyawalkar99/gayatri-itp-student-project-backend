package com.school.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StudentDTO {

	private Integer studId;

	@NotBlank
	@Size(min = 5, max = 100, message = "Name must be between 5 and 100 characters")
	private String studName;

	@NotBlank
	@Size(min = 5, max = 100, message = "email must be between 5 and 100 characters")
	private String studEmail;

	@NotBlank
	private String studCourse;

	private String studPhoto;

}
