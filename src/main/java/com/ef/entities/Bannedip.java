package com.ef.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Bannedip {
	
	@Id
	private String ip;
	private Integer requests;
	private String note;

}
