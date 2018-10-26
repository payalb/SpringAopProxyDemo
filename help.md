Aspect: service: Logging, transaction management
advice: action taken by aspect: around, before, after,
pointcut: predicate that matches join-point eg: execution of method with a particular name
target: business object
AOP proxy: An object created by the AOP framework in order to implement the aspect contracts (advise method executions and so on). In the Spring Framework, an AOP proxy is a JDK dynamic proxy or a CGLIB proxy.
Weaving: linking aspects with other application types or objects to create an advised object. This can be done at compile time (using the AspectJ compiler, for example), load time, or at runtime. Spring AOP, like other pure Java AOP frameworks, performs weaving at runtime.

Spring AOP includes the following types of advice:

Before advice: Advice that runs before a join point but that does not have the ability to prevent execution flow proceeding to the join point (unless it throws an exception).

After returning advice: Advice to be run after a join point completes normally (for example, if a method returns without throwing an exception).

After throwing advice: Advice to be executed if a method exits by throwing an exception.

After (finally) advice: Advice to be executed regardless of the means by which a join point exits (normal or exceptional return).

Around advice: Advice that surrounds a join point such as a method invocation. This is the most powerful kind of advice. Around advice can perform custom behavior before and after the method invocation. It is also responsible for choosing whether to proceed to the join point or to shortcut the advised method execution by returning its own return value or throwing an exception.



Spring AOP defaults to using standard JDK dynamic proxies for AOP proxies. This enables any interface (or set of interfaces) to be proxied.

Spring AOP can also use CGLIB proxies. This is necessary to proxy classes rather than interfaces. By default, CGLIB is used if a business object does not implement an interface. 


Java Proxy
Proxy is a design pattern. We create and use proxy objects when we want to add or modify some functionality of an already existing class. The proxy object is used instead of the original one. Usually, the proxy objects have the same methods as the original one and in Java proxy classes usually extend the original class. The proxy has a handle to the original object and can call the method on that.

the proxy class does only proxying and the actual behavior modification is implemented in handlers. When the proxy object is invoked instead of the original object, the proxy decides if it has to invoke the original method or some handler. The handler may do its task and may also call the original method.

The proxy class is generated during run-time, but the handler invoked by the proxy class can be coded in the normal source code and compiled along the code of the whole program (compile time).
The easiest way to do this is to use the java.lang.reflect.Proxy class, which is part of the JDK. That class can create a proxy class or directly an instance of it. The use of the Java built-in proxy is easy. All you need to do is implement a java.lang.InvocationHandler , so that the proxy object can invoke it. The InvocationHandler interface is extremely simple. It contains only one method: invoke(). When invoke() is invoked, the arguments contain the original object, which is proxied, the method that was invoked (as a reflection Method object) and the object array of the original arguments. 


As a special case, you can create an invocation handler and a proxy of an interface that does not have any original object. Even more, it is not needed to have any class to implement the interface in the source code. The dynamically created proxy class will implement the interface.

What should you do if the class you want to proxy does not implement an interface? In that case, you have to use some other proxy implementation.



cglib:


<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>3.2.4</version>
</dependency>


Classes in Java are loaded dynamically at runtime. Cglib is using this feature of Java language to make it possible to add new classes to an already running Java program.

Hibernate uses cglib for generation of dynamic proxies. For example, it will not return full object stored in a database but it will return an instrumented version of stored class that lazily loads values from the database on demand.

Popular mocking frameworks, like Mockito, use cglib for mocking methods. The mock is an instrumented class where methods are replaced by empty implementations.

If the target object to be proxied implements at least one interface then a JDK dynamic proxy will be used. All of the interfaces implemented by the target type will be proxied. If the target object does not implement any interfaces then a CGLIB proxy will be created.

If you want to force the use of CGLIB proxying (for example, to proxy every method defined for the target object, not just those implemented by its interfaces) you can do so. However, there are some issues to consider:

final methods cannot be advised, as they cannot be overriden.

You will need the CGLIB 2 binaries on your classpath, whereas dynamic proxies are available with the JDK. Spring will automatically warn you when it needs CGLIB and the CGLIB library classes are not found on the classpath.

The constructor of your proxied object will be called twice. This is a natural consequence of the CGLIB proxy model whereby a subclass is generated for each proxied object. For each proxied instance, two objects are created: the actual proxied object and an instance of the subclass that implements the advice. This behavior is not exhibited when using JDK proxies. Usually, calling the constructor of the proxied type twice, is not an issue, as there are usually only assignments taking place and no real logic is implemented in the constructor.

To force the use of CGLIB proxies set the value of the proxy-target-class attribute of the <aop:config> element to true:

<aop:config proxy-target-class="true">
    <!-- other beans defined here... -->
</aop:config>

To force CGLIB proxying when using the @AspectJ autoproxy support, set the 'proxy-target-class' attribute of the <aop:aspectj-autoproxy> element to true:

<aop:aspectj-autoproxy proxy-target-class="true"/>



 AspectJ Runtime
When running an AspectJ program, the classpath should contain the classes and aspects along with the AspectJ runtime library aspectjrt.jar:


<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.8.9</version>
</dependency>

Besides the AspectJ runtime dependency, we will also need to include the aspectjweaver.jar to introduce advice to Java class at load time:


<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.8.9</version>
</dependency>

AspectJ provides an implementation of AOP and has three core concepts:

Join Point
Pointcut
Advice

Advantages

Very powerful, can do nearly anything you might need
Powerful pointcut expressions for defining where to inject an advice and when to activate it (including some run-time checks) – fully enables DRY, i.e. write once & inject many times
Both build-time and load-time code injection (weaving)
Disadvantages

The modified code depends on the AspectJ runtime library
The pointcut expressions are very powerful but it might be difficult to get them right and there isn’t much support for “debugging” them though the AJDT plugin is partially able to visualize their effects
It will likely take some time to get started though the basic usage is pretty simple (using @Aspect, @Around, and a simple pointcut expression, as we will see in the example)


Compile-Time Weaving
The simplest approach of weaving is compile-time weaving. When we have both the source code of the aspect and the code that we are using aspects in, the AspectJ compiler will compile from source and produce a woven class files as output. Afterward, upon execution of your code, the weaving process output class is loaded into JVM as a normal Java class.

We use Mojo’s AspectJ Maven Plugin to weave AspectJ aspects into our classes using the AspectJ compiler.

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>aspectj-maven-plugin</artifactId>
    <version>1.7</version>
    <configuration>
        <complianceLevel>1.8</complianceLevel>
        <source>1.8</source>
        <target>1.8</target>
        <showWeaveInfo>true</showWeaveInfo>
        <verbose>true</verbose>
        <Xlint>ignore</Xlint>
        <encoding>UTF-8 </encoding>
    </configuration>
    <executions>
        <execution>
            <goals>
                <!-- use this goal to weave all your main classes -->
                <goal>compile</goal>
                <!-- use this goal to weave all your test classes -->
                <goal>test-compile</goal>
            </goals>
        </execution>
    </executions>
</plugin>


Post-Compile Weaving
Post-compile weaving (also sometimes called binary weaving) is used to weave existing class files and JAR files. As with compile-time weaving, the aspects used for weaving may be in source or binary form, and may themselves be woven by aspects.

In order to do this with Mojo’s AspectJ Maven Plugin we need do setup all the JAR files we would like to weave in the plugin configuration:

1
2
3
4
5
6
7
8
9
10
11
12
<configuration>
    <weaveDependencies>
        <weaveDependency>  
            <groupId>org.agroup</groupId>
            <artifactId>to-weave</artifactId>
        </weaveDependency>
        <weaveDependency>
            <groupId>org.anothergroup</groupId>
            <artifactId>gen</artifactId>
        </weaveDependency>
    </weaveDependencies>
</configuration>
The JAR files containing the classes to weave must be listed as <dependencies/> in the Maven project and listed as <weaveDependencies/> in the <configuration> of the AspectJ Maven Plugin.

 Load-Time Weaving
Load-time weaving is simply binary weaving deferred until the point that a class loader loads a class file and defines the class to the JVM.

To support this, one or more “weaving class loaders” are required. These are either provided explicitly by the run-time environment or enabled using a “weaving agent”.


Enabling Load-Time Weaving
AspectJ load-time weaving can be enabled using AspectJ agent that can get involved in the class loading process and weave any types before they are defined in the VM. We specify the javaagent option to the JVM -javaagent:pathto/aspectjweaver.jar or using Maven plugin to configure the javaagent :

1
2
3
4
5
6
7
8
9
10
11
12
13
14
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.10</version>
    <configuration>
        <argLine>
            -javaagent:"${settings.localRepository}"/org/aspectj/
            aspectjweaver/${aspectj.version}/
            aspectjweaver-${aspectj.version}.jar
        </argLine>
        <useSystemClassLoader>true</useSystemClassLoader>
        <forkMode>always</forkMode>
    </configuration>
</plugin>
7.2. Configuration Weaver
AspectJ’s load-time weaving agent is configured by the use of aop.xml files. It looks for one or more aop.xml files on the classpath in the META-INF directory and aggregates the contents to determine the weaver configuration.

An aop.xml file contains two key sections:

Aspects: defines one or more aspects to the weaver and controls which aspects are to be used in the weaving process. The aspects element may optionally contain one or more include and exclude elements (by default, all defined aspects are used for weaving)
Weaver: defines weaver options to the weaver and specifies the set of types that should be woven. If no include elements are specified then all types visible to the weaver will be woven
Let’s configure an aspect to the weaver:

1
2
3
4
5
6
7
8
<aspectj>
    <aspects>
        <aspect name="com.baeldung.aspectj.AccountAspect"/>
        <weaver options="-verbose -showWeaveInfo">
            <include within="com.baeldung.aspectj.*"/>
        </weaver>
    </aspects>
</aspectj>
As we can see, we have configured an aspect that points to the AccountAspect, and only the source code in the com.baeldung.aspectj package will be woven by AspectJ.

Due to the proxy-based nature of Spring’s AOP framework, calls within the target object are, by definition, not intercepted. For JDK proxies, only public interface method calls on the proxy can be intercepted. With CGLIB, public and protected method calls on the proxy are intercepted (and even package-visible methods, if necessary). However, common interactions through proxies should always be designed through public signatures.

Note that pointcut definitions are generally matched against any intercepted method. If a pointcut is strictly meant to be public-only, even in a CGLIB proxy scenario with potential non-public interactions through proxies, it needs to be defined accordingly.

If your interception needs include method calls or even constructors within the target class, consider the use of Spring-driven native AspectJ weaving instead of Spring’s proxy-based AOP framework. This constitutes a different mode of AOP usage with different characteristics, so be sure to make yourself familiar with weaving before making a decision.



Enabling @AspectJ Support with Java Configuration
To enable @AspectJ support with Java @Configuration, add the @EnableAspectJAutoProxy annotation, as the following example shows:

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

}

Enabling @AspectJ Support with XML Configuration
To enable @AspectJ support with XML-based configuration, use the aop:aspectj-autoproxy element, as the following example shows:

<aop:aspectj-autoproxy/>

@Aspect
public class NotVeryUsefulAspect {

}
Aspects (classes annotated with @Aspect) can have methods and fields, the same as any other class. They can also contain pointcut, advice, and introduction (inter-type) declarations.

 note that the @Aspect annotation is not sufficient for autodetection in the classpath. For that purpose, you need to add a separate @Component annotation (or, alternatively, a custom stereotype annotation that qualifies, as per the rules of Spring’s component scanner).
 
 
 Spring AOP supports the following AspectJ pointcut designators (PCD) for use in pointcut expressions:

execution: For matching method execution join points. This is the primary pointcut designator to use when working with Spring AOP.

within: Limits matching to join points within certain types (the execution of a method declared within a matching type when using Spring AOP).

this: Limits matching to join points (the execution of methods when using Spring AOP) where the bean reference (Spring AOP proxy) is an instance of the given type.

target: Limits matching to join points (the execution of methods when using Spring AOP) where the target object (application object being proxied) is an instance of the given type.

args: Limits matching to join points (the execution of methods when using Spring AOP) where the arguments are instances of the given types.

@target: Limits matching to join points (the execution of methods when using Spring AOP) where the class of the executing object has an annotation of the given type.

@args: Limits matching to join points (the execution of methods when using Spring AOP) where the runtime type of the actual arguments passed have annotations of the given types.

@within: Limits matching to join points within types that have the given annotation (the execution of methods declared in types with the given annotation when using Spring AOP).

@annotation: Limits matching to join points where the subject of the join point (the method being executed in Spring AOP) has the given annotation.

Spring AOP is a proxy-based system and differentiates between the proxy object itself (which is bound to this) and the target object behind the proxy (which is bound to target).

You can combine pointcut expressions can be combined by using &&, || and !

The execution of any public method:

execution(public * *(..))
The execution of any method with a name that begins with set:

execution(* set*(..))
The execution of any method defined by the AccountService interface:

execution(* com.xyz.service.AccountService.*(..))
The execution of any method defined in the service package:

execution(* com.xyz.service.*.*(..))
The execution of any method defined in the service package or one of its sub-packages:

execution(* com.xyz.service..*.*(..))
Any join point (method execution only in Spring AOP) within the service package:

within(com.xyz.service.*)
Any join point (method execution only in Spring AOP) within the service package or one of its sub-packages:

within(com.xyz.service..*)
Any join point (method execution only in Spring AOP) where the proxy implements the AccountService interface:

this(com.xyz.service.AccountService)
'this' is more commonly used in a binding form. See the section on Declaring Advice for how to make the proxy object available in the advice body.
Any join point (method execution only in Spring AOP) where the target object implements the AccountService interface:

target(com.xyz.service.AccountService)
'target' is more commonly used in a binding form. See the Declaring Advice section for how to make the target object available in the advice body.
Any join point (method execution only in Spring AOP) that takes a single parameter and where the argument passed at runtime is Serializable:

args(java.io.Serializable)
'args' is more commonly used in a binding form. See the Declaring Advice section for how to make the method arguments available in the advice body.
Note that the pointcut given in this example is different from execution(* *(java.io.Serializable)). The args version matches if the argument passed at runtime is Serializable, and the execution version matches if the method signature declares a single parameter of type Serializable.

Any join point (method execution only in Spring AOP) where the target object has a @Transactional annotation:

@target(org.springframework.transaction.annotation.Transactional)
You can also use '@target' in a binding form. See the Declaring Advice section for how to make the annotation object available in the advice body.
Any join point (method execution only in Spring AOP) where the declared type of the target object has an @Transactional annotation:

@within(org.springframework.transaction.annotation.Transactional)
You can also use '@within' in a binding form. See the Declaring Advice section for how to make the annotation object available in the advice body.
Any join point (method execution only in Spring AOP) where the executing method has an @Transactional annotation:

@annotation(org.springframework.transaction.annotation.Transactional)
You can also use '@annotation' in a binding form. See the Declaring Advice section for how to make the annotation object available in the advice body.
Any join point (method execution only in Spring AOP) which takes a single parameter, and where the runtime type of the argument passed has the @Classified annotation:

@args(com.xyz.security.Classified)
You can also use '@args' in a binding form. See the Declaring Advice section how to make the annotation object(s) available in the advice body.
Any join point (method execution only in Spring AOP) on a Spring bean named tradeService:

bean(tradeService)
Any join point (method execution only in Spring AOP) on Spring beans having names that match the wildcard expression *Service:

bean(*Service)