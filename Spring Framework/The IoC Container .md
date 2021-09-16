# 1. The IoC Container

> This chapter covers Spring's Inversion of Control (Ioc) container.

## 1.1 Introduction to the Spring IoC Container and Beans

IoC is also known as dependency injection. It is a process whereby objects define their dependencies (that is, the other objects they work with) only through constructor arguments, arguments to a factory method, or properties that are set on the object instance after it is constructed or returned from a factory method. The container then injects those dependencies when it creates the bean. This process is fundamentally the inverse (hence the name, Inversion of Control) of the bean itself controlling the instantiation or location of its dependencies by using direct construction of classes or a mechanism such as the Service Locator pattern.

`BeanFactory`: provide an advanced configuration mechanism capable of managing any type of object.

`beans`: the objects that form the backbone of your application and that are managed by the Spring IoC container.

Beans, and the dependencies among them, are reflected in the configuration metadata used by a container.
