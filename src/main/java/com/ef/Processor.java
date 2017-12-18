package com.ef;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ef.entities.Bannedip;
import com.ef.entities.Logline;
import com.ef.repositories.BannedipRepository;
import com.ef.repositories.LogRepository;

import ef.com.types.DurationType;

@Component
public class Processor {

	@Autowired
	LogRepository logRepository;

	@Autowired
	BannedipRepository bannedipRepository;

	@Value("${tool.loadData}")
	private boolean loadData;

	static Logger logger = LoggerFactory.getLogger(Processor.class);

	String accesslogPath;
	Date startDate;
	DurationType duration;
	int threshold;

	public void setAccessLogPath(String _accesslogPath) {
		accesslogPath = _accesslogPath;
	}

	public void setStartDate(Date _startDate) {
		startDate = _startDate;
	}

	public void setDuration(DurationType _duration) {
		duration = _duration;
	}

	public void setThreshold(int _threshold) {
		threshold = _threshold;
	}

	public <T> Stream<T> readCsv(String file, Class<T> type) {
		Stream<T> result = Stream.empty();
		try {
			BufferedReader br = Files.newBufferedReader(Paths.get(file));
			Stream<String> lines = br.lines();
			result = lines.map(line -> {
				return createInstance(line, type);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private <T> T createInstance(String line, Class<T> type) {
		T instance = null;
		try {
			Constructor<T> constructor = type.getConstructor(String.class);
			instance = constructor.newInstance(line);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instance;
	}

	public void run() {
		if (loadData) {
			readCsv(accesslogPath, Logline.class).forEach(loglineEntity -> {
				logRepository.save(loglineEntity);
			});
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		String endDateStr = df.format(startDate);
		List<Bannedip> result = null;
		switch (duration.durationType()) {
		case 0:
			result = logRepository.getIpByHourlyThreshold(endDateStr, threshold);
			break;
		case 1:
			result = logRepository.getIpByDailyThreshold(endDateStr, threshold);
			break;
		}
		if (result != null) {
			String message = String.format("made more than %d requests in an %s", threshold, duration.dimension());
			for (Bannedip bannedip : result) {
				bannedip.setNote(message);
				bannedipRepository.save(bannedip);
				System.out.println(bannedip.getIp());
			}
		}
	}

}
