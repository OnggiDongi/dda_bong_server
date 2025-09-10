package com.hana7.ddabong.config;

import com.hana7.ddabong.entity.Certification;
import com.hana7.ddabong.entity.User;
import com.hana7.ddabong.repository.CertificationRepository;
import com.hana7.ddabong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class CertificationBatchConfig {

	private final UserRepository userRepository;
	private final CertificationRepository certificationRepository;

	@Bean
	public Job certificationJob(JobRepository jobRepository, Step checkCertificationStep) {
		return new JobBuilder("certificationJob", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(checkCertificationStep)
				.build();
	}

	@Bean
	public Step checkCertificationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("checkCertificationStep", jobRepository)
				.<User, Certification>chunk(10, transactionManager)
				.reader(userCertificationReader())
				.processor(userCertificationProcessor())
				.writer(certificationWriter())
				.build();
	}

	@Bean
	public ItemReader<User> userCertificationReader() {
		return new ListItemReader<>(
				userRepository.findAll()
		);
	}

	@Bean
	public ItemProcessor<User, Certification> userCertificationProcessor() {
		return user -> {
			int hours = user.getTotalHour();

			// 가장 최근 단위의 50시간 기준 계산
			int milestone = (hours / 50) * 50;

			// 50시간 이하인 것은 패스
			if (milestone == 0) return null;

			boolean exists = certificationRepository.existsByUserAndHour(user, milestone);
			if (!exists) {
				return Certification.builder()
						.user(user)
						.hour(milestone)
						.build();
			} else {
				return null;
			}
		};
	}

	@Bean
	public ItemWriter<Certification> certificationWriter() {
		return new RepositoryItemWriterBuilder<Certification>()
				.repository(certificationRepository)
				.methodName("save")
				.build();
	}
}
