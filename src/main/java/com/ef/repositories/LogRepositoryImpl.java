package com.ef.repositories;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ef.entities.Bannedip;

@Repository
public class LogRepositoryImpl implements LogRepositoryCustom {
	
	static Logger logger = LoggerFactory.getLogger(LogRepositoryImpl.class);

	@PersistenceContext
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Bannedip> getIpByDailyThreshold(String startDateStr, int threshold) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		Query query = null;
		try {
			Date dateStart = df.parse(startDateStr);
			Calendar cal = Calendar.getInstance();
		    cal.setTime(dateStart); 
		    cal.add(Calendar.DAY_OF_MONTH, 1);
		    Date dateEnd = cal.getTime();
		    String endDateStr = df.format(dateEnd);
			String sqlQuery = "SELECT count(id) AS requests, ip, '' as note FROM logline WHERE date BETWEEN '%s' AND '%s' GROUP BY ip HAVING requests >= %d";
			sqlQuery = String.format(sqlQuery, startDateStr, endDateStr, threshold);
			query = entityManager.createNativeQuery(sqlQuery, Bannedip.class);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bannedip> getIpByHourlyThreshold(String startDateStr, int threshold) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		Query query = null;
		try {
			Date dateStart = df.parse(startDateStr);
			Calendar cal = Calendar.getInstance();
		    cal.setTime(dateStart); 
		    cal.add(Calendar.HOUR_OF_DAY, 1);
		    Date dateEnd = cal.getTime();
		    String endDateStr = df.format(dateEnd);
			String sqlQuery = "SELECT count(id) AS requests, ip, '' as note FROM logline WHERE date BETWEEN '%s' AND '%s' GROUP BY ip HAVING requests >= %d";
			sqlQuery = String.format(sqlQuery, startDateStr, endDateStr, threshold);
			query = entityManager.createNativeQuery(sqlQuery, Bannedip.class);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return query.getResultList();
	}

}
