package com.java.service.impl;

import org.springframework.stereotype.Service;

import com.java.model.Account;

@Service("accountService")
public class AccountServiceImpl implements AccountService {
	
	public AccountServiceImpl(){
		super();
		System.out.println("Constructor invoked!!");
	}

	/* (non-Javadoc)
	 * @see com.infotech.service.impl.AccountService#updateAccountBalance(com.infotech.model.Account, java.lang.Long)
	 */
	@Override
	public String updateAccountBalance(Account account,Long amount){
		return "Account No:"+account.getAccountNumber()+", Amount:"+amount;
	}
}
