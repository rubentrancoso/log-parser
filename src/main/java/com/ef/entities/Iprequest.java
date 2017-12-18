package com.ef.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Iprequest {
	
	@Id
	private String ip;
	private Integer requests;

}
