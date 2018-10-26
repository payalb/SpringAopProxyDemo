package com.java.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.java.model.Account;
import com.java.service.impl.AccountService;
import com.java.service.impl.AccountServiceImpl;

public class JDkProxyDemo {

	public static void main(String[] args) {
		/*
		 * The easiest way to do this is to use the java.lang.reflect.Proxy class, which
		 * is part of the JDK. That class can create a proxy class or directly an
		 * instance of it. The use of the Java built-in proxy is easy. All you need to
		 * do is implement a java.lang.InvocationHandler , so that the proxy object can
		 * invoke it. The InvocationHandler interface is extremely simple. It contains
		 * only one method: invoke(). When invoke() is invoked, the arguments contain
		 * the original object, which is proxied, the method that was invoked (as a
		 * reflection Method object) and the object array of the original arguments.
		 * 
		 * 
		 * As a special case, you can create an invocation handler and a proxy of an
		 * interface that does not have any original object. Even more, it is not needed
		 * to have any class to implement the interface in the source code. The
		 * dynamically created proxy class will implement the interface.
		 * 
		 * What should you do if the class you want to proxy does not implement an
		 * interface? In that case, you have to use some other proxy implementation.
		 */
		AccountService service1 =new AccountServiceImpl();
		AccountService service = (AccountService) Proxy.newProxyInstance(JDkProxyDemo.class.getClassLoader(),
				new Class[] { AccountService.class },new MyProxyHandler(service1));

		System.out.println(service.updateAccountBalance(new Account("3323", "savings"), 24l));
	}

}

class MyProxyHandler implements InvocationHandler{

			
			private Object target;
			public MyProxyHandler(Object t){
				this.target= t;
			}

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						System.out.println(method.getName());
						System.out.println(args);
						System.out.println(method.invoke(target, args));
						return "Implemented by jdk proxy";
					}
				
}
