package com.java.client;

import com.java.model.Account;
import com.java.service.impl.AccountService;
import com.java.service.impl.AccountServiceImpl;
/*AspectJ is a full-blown AOP tool, it can do nearly anything you might want, including the modification of static methods, addition of new fields, addition of an interface to a class’ list of implemented interfaces etc.

The syntax of AspectJ advices comes in two flavours, one is a superset of Java syntax with additional keywords like aspect and pointcut, the other one – called @AspectJ – is standard Java 5 with annotations such as @Aspect, @Pointcut, @Around*/
public class AspectJrtDemo {
	public static void main(String[] args) {
		AccountService obj= new AccountServiceImpl();
		System.out.println(obj.updateAccountBalance(new Account("234432","savings"), 34543543l))	;
	}
}
