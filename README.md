# Loxpression
# 一、背景介绍
Loxpression是一款面向Java平台的高性能、轻量级表达式计算引擎，旨在提高用户系统在不同业务场景下的扩展能力。
# 二、调用说明
## 求值模式
支持+、-、*、/、**【指数运算】、<、>、<=、>=、==、!=、%、&&、||、!、等操作符。支持Excel风格的if(cond, thenBranch, elseBranch)条件函数。
```java
Environment env = new Environment();  
env.put("a", 1);
env.put("b", 2);
env.put("c", 3);
LoxRunner runner = new LoxRunner();
Object r = runner.execute("a + b * c - 100 / 5 ** 2 ** 1", env);
System.out.println(r);
		
r = runner.execute("a + b * c >= 6", env);
System.out.println(r);
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
Environment env = new Environment();
env.put("m", 2);
env.put("n", 4);
env.put("w", 6);
runner.execute(srcs, env);

System.out.println(env.get("x"));
System.out.println(env.get("a"));
System.out.println(env.get("b"));
System.out.println(env.get("c"));
```
