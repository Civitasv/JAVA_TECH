# 1. The IoC Container

> This chapter covers Spring's Inversion of Control (Ioc) container.

## 1.1 Introduction to the Spring IoC Container and Beans

IoC is also known as dependency injection. It is a process whereby objects define their dependencies (that is, the other objects they work with) only through constructor arguments, arguments to a factory method, or properties that are set on the object instance after it is constructed or returned from a factory method. The container then injects those dependencies when it creates the bean. This process is fundamentally the inverse (hence the name, Inversion of Control) of the bean itself controlling the instantiation or location of its dependencies by using direct construction of classes or a mechanism such as the Service Locator pattern.

`BeanFactory`: provide an advanced configuration mechanism capable of managing any type of object.

`beans`: the objects that form the backbone of your application and that are managed by the Spring IoC container.

Beans, and the dependencies among them, are reflected in the configuration metadata used by a container.

## 1.2 Container Overview

The `org.springframework.context.ApplicationContext` interface represents the Spring IoC container and is reponsible for instanitaing, configuring, and assembling the beans.

The following diagram shows a high-level view of how Spring works. Your application classes are combined with configuration metadata so that, after the `ApplicationContext` is created and initialized, you have a fully configured and executable system or application.

![The Spring IoC container](https://docs.spring.io/spring-framework/docs/current/reference/html/images/container-magic.png)

### 1.2.1 Configuration Metadata

This configuration metadata represents how you, as an application developer, tell the Spring container to instantiate, configure, and assemble the objects in your application. Spring configuration consists of at least one and typically more than one bean definition that the container must manage. XML-based configuration metadata configures these beans as `<bean/>` elements inside a top-level `<beans/>` element. Java configuration typically uses `@Bean`-annotated methods within a `@Configuration` class.

An example of the basic structure of XML-based configuration metadata:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="..." class="...">  
        <!-- collaborators and configuration for this bean go here -->
    </bean>

    <bean id="..." class="...">
        <!-- collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions go here -->

</beans>
```

1. `id` attribute is a string that identifies the individual bean definition.
2. `class` attribute defines the type of the bean and uses the fully qualified classname.

### 1.2.2 Instantiating a Container

The `ApplicationContext` class is initiate by resource strings.

```java
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");
```

The `services.xml` as follows:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- services -->

    <bean id="petStore" class="org.springframework.samples.jpetstore.services.PetStoreServiceImpl">
        <property name="accountDao" ref="accountDao"/>
        <property name="itemDao" ref="itemDao"/>
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions for services go here -->

</beans>
```

And the `daos.xml` is:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="accountDao"
        class="org.springframework.samples.jpetstore.dao.jpa.JpaAccountDao">
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <bean id="itemDao" class="org.springframework.samples.jpetstore.dao.jpa.JpaItemDao">
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions for data access objects go here -->
```

From these two files, we can see that the `petStore` has two `property`, one `accountDao`, another `itemDao`, which are defined in `daos.xml`. By this way, we can express the dependency between collaborating objects.

#### Composing XML-based Configuration Metadata

It can be useful to have bean definitions span multiple XML files. Often, each individual XML configuration file represents a logical layer or module in program architecture.

We can use the constructor of `ApplicationContext` to realize it, and we can also use one or more occurrences of the `<import/>` elements to load bean definitions from another file or files. For example:

```xml
<beans>
    <import resource="services.xml"/>
    <import resource="resources/messageSource.xml"/>
    <import resource="/resources/themeSource.xml"/>

    <bean id="bean1" class="..."/>
    <bean id="bean2" class="..."/>
</beans>
```

In this example, this XML definitions are loaded from three files: `services.xml`, `messageSource.xml` and `themeSource.xml`.

NOT RECOMMENDED: USE RELATIVE PATH.

The namespace itself provides the import directive feature. Further configuration features beyond plain bean definitions are avaliable in a selection of XML namespaces provided by Spring -- for example, the `context` and `util` namespaces.

### 1.2.3 Using the Container

The `ApplicationContext` is the interface for an advanced factory capable of maintaining a registry of different beans and their dependencies. By using the method `T getBean(String name, Class<T> requiredType)`, you can retrieve instances of your beans.

```java
// create and configure beans
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");

// retrieve configured instance
PetStoreService service = context.getBean("petStore", PetStoreService.class);

// use configured instance
List<String> userList = service.getUsernameList();
```

We can also use the class `GenericApplicationContext`, for example:

```java
GenericApplicationContext context = new GenericApplicationContext();
new XmlBeanDefinitionReader(context).loadBeanDefinitions("services.xml", "daos.xml");
context.refresh();
```

Once we get context, we can use `getBean` to retrieve instances of your beans. The `ApplicationContext` interface has a few other methods for retrieving beans, but, ideally, your application code should never use them. Indeed, your application code should have no calls to the getBean() method at all and thus have no dependency on Spring APIs at all. For example, Spring’s integration with web frameworks provides dependency injection for various web framework components such as controllers and JSF-managed beans, letting you declare a dependency on a specific bean through metadata (such as an autowiring annotation).

## 1.3 Bean Overview

Within the container itself, these beans are created with the configuration metadata that you supply to the container.

In the container, these bean definitions are represented as `BeanDefinition` objects, which contain the following metadata:

- A package-qualified class name: typically, the actual implementation class of the bean being defined.
- Bean behavioral configuration elements, which state how the bean should behave in the container(scope, lifecycle callbacks, and so forth).
- References to other beans that are needed for the bean to do its work. These references are also called collaborators or dependencies.
- Other configuration settings to set in the newly created object--for example, the size limit of the pool or the number of connections to use in a bean that manages a connection pool.

This metadata translates to a set of properties that make up each bean definition. The following table describes these properties:

```xml
<bean class="" name="" scope="" autowire="" lazy-init="" init-method="" destroy-method="">
    <property name="" />
    <constructor-arg/>

</bean>
```

> Bean metadata and manually supplied singleton instances need to be registered as early as possible, in order for the container to properly reason about them during autowiring and other introspection steps. While overriding existing metadata and existing singleton instances is supported to some degree, the registration of new beans at runtime (concurrently with live access to the factory) is not officially supported and may lead to concurrent access exceptions, inconsistent state in the bean container, or both.

### 1.3.1 Naming Beans

We need one or more identifiers to identify the beans in container. Surely, these identifiers must be unique. Usually, a bean only need one identifier.

In XML-based configuraion metadata, we use `id` and `name` or both to specify the bean identifiers.

You are not required to supply a `name` or an `id` for a bean. If you do not supply a `name` or `id` explicitly, the container generates a unique name for that bean. However, if you want to refer to that bean by name, through the use of the `ref` element or a Service Locator style lookup, **you must provide a name**. The reason that not supplying a name are related to using `inner beans` and `autowiring collaborators`.

> With component scanning in the classpath, Spring generates bean names for unnamed components, following the rules described earlier: essentially, taking the simple class name and turning its initial character to lower-case. However, in the (unusual) special case when there is more than one character and both the first and second characters are upper case, the original casing gets preserved. These are the same rules as defined by java.beans.Introspector.decapitalize (which Spring uses here).

#### Aliasing a Bean outside the Bean Definition

In a bean definition itself, you can supply more than one name for the bean, by using a combination of up to one name specified by the `id` attribute and any number of other names in the `name` attribute. These names can be equibalent aliases to the same bean. **In some situations, it can be useful, such as letting each component in an application refer to a common dependency by using a bean name that is specific to that component itself.**

In XML-based configuration metadata, we can use the `<alias/>` element to define alias for a bean.

```xml
<alias name="fromName" alias="toName">
```

In this case, a bean named `fromName` can be referred to as `toName`.















