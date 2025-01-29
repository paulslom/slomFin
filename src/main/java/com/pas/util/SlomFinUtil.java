package com.pas.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SlomFinUtil
{
	static Logger log = LogManager.getLogger(SlomFinUtil.class);

	public static Map<Integer, List<Integer>> invTypeTrxTypeMap = new HashMap<>();
	public static Map<Integer, List<Integer>> acctTypeInvTypeMap = new HashMap<>();
	public static Map<Integer, List<Integer>> acctTypeTrxTypeMap = new HashMap<>();
	
	public SlomFinUtil()
	{
		invTypeTrxTypeMap.put(1, Arrays.asList(1,2,9,10,11,12,13));
		invTypeTrxTypeMap.put(2, Arrays.asList(1,3,9,11,12,13,16));
		invTypeTrxTypeMap.put(3, Arrays.asList(1,2,9,10,11,12,13));
		invTypeTrxTypeMap.put(5, Arrays.asList(2,4,5,6,7,8,12,13,15,17));
		invTypeTrxTypeMap.put(6, Arrays.asList(1,11));
		
		acctTypeInvTypeMap.put(1, Arrays.asList(1,2,3,5,7));
		acctTypeInvTypeMap.put(2, Arrays.asList(1,3,5));
		acctTypeInvTypeMap.put(3, Arrays.asList(1,2,3,5));
		acctTypeInvTypeMap.put(4, Arrays.asList(5));
		acctTypeInvTypeMap.put(5, Arrays.asList(5));
		acctTypeInvTypeMap.put(6, Arrays.asList(6));
		acctTypeInvTypeMap.put(7, Arrays.asList(3,5));
		acctTypeInvTypeMap.put(8, Arrays.asList(5));
		acctTypeInvTypeMap.put(9, Arrays.asList(5));
		acctTypeInvTypeMap.put(10, Arrays.asList(5));
		acctTypeInvTypeMap.put(11, Arrays.asList(5));
		acctTypeInvTypeMap.put(12, Arrays.asList(5));
		acctTypeInvTypeMap.put(13, Arrays.asList(5));
		acctTypeInvTypeMap.put(14, Arrays.asList(1,2,3,5));
		acctTypeInvTypeMap.put(15, Arrays.asList(1,3,5));
		acctTypeInvTypeMap.put(16, Arrays.asList(1,2,3,5));
		
		//124 of these - still need to do
		acctTypeTrxTypeMap.put(null, null);
	}
	
}