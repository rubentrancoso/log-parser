package com.ef.repositories;

import java.util.List;

import com.ef.entities.Bannedip;

public interface LogRepositoryCustom {
	List<Bannedip> getIpByDailyThreshold(String startDate, int threshold);
	List<Bannedip> getIpByHourlyThreshold(String startDate, int threshold);
}
