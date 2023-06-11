package com.example.mongodemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MongoDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(
			StudentRepository studentRepository,
			MongoTemplate mongoTemplate) {
		return args -> {
			Address address = new Address(
					"Canada",
					"4711",
					"Ottawa"
			);

			String email = "sidekick.john@gmail.com";
			Student student = new Student(
					"john",
					"sidekick",
					email,
					Gender.MALE,
					address,
					List.of("Computer Science", "Maths"),
					BigDecimal.TEN,
					LocalDateTime.now()
			);

//			usingMongoTemplate(studentRepository, mongoTemplate, email, student);

			studentRepository.findStudentByEmail(email).ifPresentOrElse(s -> {
				System.out.println(s + " already exists!");
			}, () -> {
				System.out.println("Inserting student " + student);
				studentRepository.insert(student);
			});

		};
	}

	private static void usingMongoTemplate(StudentRepository studentRepository, MongoTemplate mongoTemplate, String email, Student student) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));

		List<Student> students = mongoTemplate.find(query, Student.class);

		if (students.size() > 1) {
			throw new IllegalStateException("found more than one student with email: " + email);
		}

		if (students.isEmpty()) {
			System.out.println("Inserting student " + student);
			studentRepository.insert(student);
		} else {
			System.out.println(student + " already exists!");
		}
	}
}
