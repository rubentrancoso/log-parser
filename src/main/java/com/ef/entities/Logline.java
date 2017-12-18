package com.ef.entities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Logline {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private Date date;
	
	private String ip;
	
	private String method;

	private int httpstatus;
	
	private String agent;

	public Logline() {}
	
	public Logline(String csvline) {
		String[] value = csvline.replace('|', '#').split("#");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		try {
			date = df.parse(value[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ip = value[1];
		method = value[2].replace("\"", "");
		httpstatus = Integer.parseInt(value[3]);
		agent = value[4].replace("\"", "");
	}

}
