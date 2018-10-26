/*package com.infotech.service.impl;

import com.infotech.model.Account;

import com.infotech.client.aspect;

public aspect BeforeAspect{
	pointcut callUpdateAccountNumber(Account account, Long number, AccountServiceImpl impl) : call(public String AccountServiceImpl.updateAccountNumber(Account ,Long )) && target(impl);
	
	before(Account account, Long number,AccountServiceImpl impl) : callUpdateAccountNumber(account, number, impl) {
		System.out.println("going to call update accoutn number");
	}
	
	after(Account account, Long number,AccountServiceImpl impl): callUpdateAccountNumber(account, number, impl) {
		System.out.println("after calling update accoutn number");
	}
	
	after(Account account, Long number,AccountServiceImpl impl) returning(String o) : callUpdateAccountNumber(account, number, impl) {
		System.out.println("after calling update accoutn number");
	}
	
	after(Account account, Long number,AccountServiceImpl impl) throwing(Exception e) : callUpdateAccountNumber(account, number, impl) {
		System.out.println("after throwing exception from  update accoutn number");
		
	}
}*/