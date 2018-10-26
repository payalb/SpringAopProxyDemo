package com.java.client;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.FixedValue;
import org.springframework.cglib.proxy.MethodInterceptor;

import com.java.model.Account;
import com.java.service.impl.AccountService;
import com.java.service.impl.AccountServiceImpl;

/*. The Enhancer class allows us to create a proxy by dynamically extending a AccountService class by
 *  using a setSuperclass() method from the Enhancer class:
 * The FixedValue is a callback interface that simply returns the value from the proxied method.
 *  Executing updateAccountBalance() method on a proxy returned a value specified in a proxy method.
 * 
 * */
public class CglibProxyDemo {
	public static void main(String[] arg) {

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(AccountServiceImpl.class);
		enhancer.setCallback((FixedValue) () -> "Hello from proxy");

		AccountService serviceProxy = (AccountService) enhancer.create();
		System.out.println(serviceProxy.updateAccountBalance(new Account("65435", "saving"), 74674l));

		/*
		 * The first version of our proxy has some drawbacks because we are not able to
		 * decide which method a proxy should intercept, and which method should be
		 * invoked from a superclass. We can use a MethodInterceptor interface to
		 * intercept all calls to the proxy and decide if want to make a specific call
		 * or execute a method from a superclass:
		 * 
		 * In this example, we are intercepting all calls when method signature is not
		 * from the Object class, meaning that i.e. toString() or hashCode() methods
		 * will not be intercepted. Besides that, we are intercepting only methods from
		 * AccountService that returns a String.
		 */
		Enhancer enhancer1 = new Enhancer();
		enhancer1.setSuperclass(AccountServiceImpl.class);
		enhancer1.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
			if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
				return "Hello from method interceptor callback!";
			} else {
				return proxy.invokeSuper(obj, args);
			}
		});

		AccountService serviceProxy1 = (AccountService) enhancer1.create();
		System.out.println(serviceProxy1.updateAccountBalance(new Account("65435", "saving"), 74674l));
	}
}
