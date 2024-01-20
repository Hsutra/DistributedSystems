package com.example.apple;

import com.example.apple.models.StudentModel;
import com.example.apple.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
@SpringBootApplication
public class AppleApplication implements CommandLineRunner {

	private final StudentRepository StudentRepo;

	public AppleApplication(StudentRepository StudentRepo) {
		this.StudentRepo = StudentRepo;
	}

	@Override
	public void run(String... args) throws Exception {
		List<StudentModel> students = this.loadDataFromJson();
		this.StudentRepo.saveAll(students);
	}

	private List<StudentModel> loadDataFromJson() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		InputStream inputStream = (new ClassPathResource("data.json")).getInputStream();
		StudentModel[] students = objectMapper.readValue(inputStream, StudentModel[].class);
		return Arrays.asList(students);
	}

	public static void main(String[] args) {
		SpringApplication.run(AppleApplication.class, args);
	}

}
