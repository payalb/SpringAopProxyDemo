package com.java.service.impl;

import com.java.model.Account;

public interface AccountService {

	public abstract String updateAccountBalance(Account account, Long amount);

}