# Loxpression
# 一、背景介绍
Loxpression是一款面向Java平台的高性能、轻量级表达式计算引擎，旨在提高用户系统在不同业务场景下的扩展能力。
# 二、用法说明
## 求值模式
支持+、-、*、/、**【指数运算】、<、>、<=、>=、==、!=、%、&&、||、!、等操作符。支持Excel风格的if(cond, thenBranch, elseBranch)条件函数。
```java
Environment env = new DefaultEnvironment();  
env.put("a", 1);
env.put("b", 2);
env.put("c", 3);
LoxRunner runner = new LoxRunner();
Object r = runner.execute("a + b * c - 100 / 5 ** 2 ** 1", env);
System.out.println(r); // 3.0
		
r = runner.execute("a + b * c >= 6", env);
System.out.println(r); // true
```
## 运算模式
支持表达式变量赋值运算，多个表达式批量进行运算时，支持根据表达式的依赖关系先进行排序，再运算。并且会对运算表达式之间是否有循环依赖进行检测。
```java
List<String> srcs = new ArrayList<>();
srcs.add("x = a + b * c");
srcs.add("a = m + n");
srcs.add("b = a * 2");
srcs.add("c = n + w + b");

LoxRunner runner = new LoxRunner();
Environment env = new DefaultEnvironment();
env.put("m", 2);
env.put("n", 4);
env.put("w", 6);
runner.execute(srcs, env);

System.out.println(env.get("x")); // 126
System.out.println(env.get("a")); // 6
System.out.println(env.get("b")); // 12
System.out.println(env.get("c")); // 10
```
##  定义环境
表达式求值时，对于遇到的变量，求值器会从环境对象Environment中取值，赋值表达式则会把求值的结果写回到Environment中，因此对于表达式中用到的变量，具体含义需要在Environment中进行定义：
```java
Environment env = new DefaultEnvironment();  
env.put("a", 1);
env.put("b", 2);
env.put("c", 3);
LoxRunner runner = new LoxRunner();
Object r = runner.execute("a + b * c ", env);
System.out.println(r); // 7
```
系统提供的默认环境对象为DefaultEnvironment，在执行表达式前，对于表达式中需要读取值的变量，都需要在DefaultEnvironment对象中有值。有时候需要执行的表达式数量较多，在对表达式做解析之前，业务层无法高效的把所有变量值都提前准备好，或者表达式中的变量和实际数据之间是间接的关联，这时候便可以根据需要自定义环境对象，只需继承Environment抽象类即可。参照示例:[FormEnvironment.java](https://github.com/SimonFamily/Loxpression/blob/master/src/test/java/com/loxpression/env/form/FormEnvironment.java)，以及单元测试:[FormEnvTest.java](https://github.com/SimonFamily/Loxpression/blob/master/src/test/java/com/loxpression/env/form/FormEnvTest.java)
# 三、实现方式
[todo]


