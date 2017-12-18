package com.ef.repositories;

import java.util.List;

import com.ef.entities.Iprequest;

public interface LogRepositoryCustom {
	List<Iprequest> getIpByDailyThreshold(String startDate, int threshold);
	List<Iprequest> getIpByHourlyThreshold(String startDate, int threshold);
}
