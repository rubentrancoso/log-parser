package com.ef;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.ef.repositories.BannedipRepository;
import com.ef.repositories.LogRepository;

import ef.com.types.DurationType;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories(basePackageClasses = { LogRepository.class, BannedipRepository.class })
@EntityScan(basePackages = { "com.ef.entities" })
@ComponentScan(basePackages = { "com.ef" })
public class Parser {

	@Autowired
	Processor processor;

	static Logger logger = LoggerFactory.getLogger(Parser.class);
	private static String accesslog;
	private static Date startDate;
	private static DurationType duration;
	private static int threshold;

	private static String exemplo = "   ex.: java -cp parser.jar com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100";

	@PostConstruct
	private void init() {
		processor.setAccessLogPath(accesslog);
		processor.setStartDate(startDate);
		processor.setDuration(duration);
		processor.setThreshold(threshold);
		logger.info("Executing parse.");
		processor.run();
		logger.info("Parse execution ended.");
	}

	// Program arguments: --accesslog=/path/to/file --startDate=2017-01-01.13:00:00
	// --duration=hourly --threshold=100
	public static void main(String[] args) {
		String[] parsedArgs = parseArgs(args);
		// map arguments to be used on log
		logger.info("Parser started.");
		SpringApplication.run(Parser.class, parsedArgs);
		logger.info("Parser ended.");
	}

	private static String[] parseArgs(String[] args) {
		Vector<String> newArgs = new Vector<String>();

		HashMap<String, String> result = new HashMap<String, String>();
		// Ignores any value that is not in the format --parameter=value
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--")) {
				String[] part = args[i].substring(2).split("=");
				if (part.length == 2)
					// Creates a hashmap with valid values
					result.put(part[0], part[1]);
			}
		}

		Set<String> errors = new HashSet<String>();

		// Verify and generate erros on expected values
		if (!result.containsKey("accesslog")) {
			errors.add(
					"Error: --accesslog parameter not specified. Please, specify a path for the log file to be parsed.");
		} else {
			validate_access_log(errors, result.get("accesslog"));
		}
		if (!result.containsKey("startDate")) {
			errors.add("Error: --startDate parameter not specified. Please, specify a start date.");
		} else {
			validate_start_date(errors, result.get("startDate"));
		}
		if (!result.containsKey("duration")) {
			errors.add("Error: --duration parameter not specified. Please, specify a duration.");
		} else {
			validate_duration(errors, result.get("duration"));
		}
		if (!result.containsKey("threshold")) {
			errors.add("Error: --threshold parameter not specified. Please specify a threshold.");
		} else {
			validate_threshold(errors, result.get("threshold"));
		}
		if (result.containsKey("spring.profiles.active")) {
			newArgs.add("--spring.profiles.active" + "=" + result.get("spring.profiles.active"));
		}

		if (errors.size() > 0) {
			for (String error : errors) {
				logger.error(error);
			}
			logger.error(exemplo);
			System.exit(1);
		}

		// pass back other arguments that could be needed by the application
		String[] converted = new String[newArgs.size()];
		return newArgs.toArray(converted);
	}

	private static void validate_threshold(Set<String> errors, String thresholdValue) {
		try {
			threshold = Integer.parseInt(thresholdValue);
		} catch (Exception e) {
			errors.add("Error: --threshold value must be and integer.");
		}
	}

	private static void validate_duration(Set<String> errors, String durationValue) {
		try {
			duration = DurationType.getDurationType(durationValue);
		} catch (Exception e) {
			errors.add("Error: --duration value must be second,minute,hourly,daily, weekly, monthly, yearly.");
		}
	}

	private static void validate_start_date(Set<String> errors, String startDateValue) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
			startDate = df.parse(startDateValue);
		} catch (Exception e) {
			errors.add("Error: --startDate value must be a valid date in yyyy-MM-dd.HH:mm:ss format.");
		}
	}

	private static void validate_access_log(Set<String> errors, String accessLogValue) {
		File f = new File(accessLogValue);
		if (!f.exists() && !f.isDirectory()) {
			errors.add("Error: --accesslog value must point to a log file.");
		} else {
			accesslog = accessLogValue;
		}
	}
}
