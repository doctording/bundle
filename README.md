
1. 双亲委派
2. 类加载器 + 类全限定名 确定 类


---

* 根加载器`Bootstrap ClassLoader`
  
  最顶层的加载器，其没有任何父加载器，由C++编写，主要负责虚拟机核心类库的加载，例如`java.lang`包
  
  将存放于`<JAVA_HOME>\lib`目录中的，或者被`-Xbootclasspath`参数所指定的路径中的，并且是虚拟机识别的（仅按照文件名识别，如 rt.jar 名字不符合的类库即使放在lib目录中也不会被加载）类库加载到虚拟机内存中。启动类加载器无法被Java程序直接引用。
  
* 扩展类加载器`Extension ClassLoader`
  
  扩展类加载器的父加载器是根加载器，纯Java语言实现，将`<JAVA_HOME>\lib\ext`目录下的，或者被java.ext.dirs系统变量所指定的路径中的所有类库加载。开发者可以直接使用扩展类加载器。
  
* 系统类加载器`System ClassLoader`(或应用类加载器`Application ClassLoader`)
  
  负责加载用户类路径(ClassPath)上所指定的类库，通常我们自己写的类就是由其加载的
  
* 自定义类加载器`Custom CLassLoader`
  
  所有自定义类加载器都是`ClassLoader`的直接子类或者间接子类（java.lang.ClassLoader是一个抽象类）

---

<a href='https://docs.oracle.com/javase/7/docs/api/java/lang/ClassLoader.html#findLoadedClass(java.lang.String)'>findLoadedClass方法</a>

```java
protected final Class<?> findLoadedClass(String name)

Returns the class with the given binary name if this loader has been recorded by the Java virtual machine as an initiating loader of a class with that binary name. Otherwise null is returned.

Parameters:
    name - The binary name of the class

Returns:
    The Class object, or null if the class has not been loaded

Since:
    1.1
```

<a href='https://docs.oracle.com/javase/7/docs/api/java/lang/ClassLoader.html#loadClass(java.lang.String)'>loadClass方法</a>

```java
public Class<?> loadClass(String name)
                   throws ClassNotFoundException
                   
Loads the class with the specified binary name. This method searches for classes in the same manner as the loadClass(String, boolean) method. It is invoked by the Java virtual machine to resolve class references. Invoking this method is equivalent to invoking loadClass(name, false).

Parameters:
    name - The binary name of the class

Returns:
    The resulting Class object

Throws:
    ClassNotFoundException - If the class was not found
```