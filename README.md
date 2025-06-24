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
系统提供的默认环境对象为DefaultEnvironment，在执行表达式前，对于表达式中需要读取值的变量，都需要在DefaultEnvironment对象中有值。有时候需要执行的表达式数量较多，在对表达式做解析之前，业务层无法高效的把所有变量值都提前准备好，或者表达式中的变量和实际数据之间是间接的关联，这时候便可以根据需要自定义环境对象，只需继承Environment抽象类即可。参照示例:[FormEnvironment.java](src/test/java/com/loxpression/env/form/FormEnvironment.java)，以及单元测试:[FormEnvTest.java](src/test/java/com/loxpression/env/form/FormEnvTest.java)
## 编译运行
Loxpression提供两种执行表达式的方式，一是直接执行表达式字符串，比如上文所举例子，适合表达式数量较少的情况。二是先把表达式编译为字节码(Chunk)，业务系统缓存或者存储字节码对象，后续需要执行时直接运行字节码。
- 编译表达式：
```java
LoxRunner runner = new LoxRunner();
Chunk chunk = runner.compileSrc(srcs);
```
- 运行字节码：
```java
LoxRunner runner = new LoxRunner();
Environment env = getEnv();
runner.runChunk(chunk, env);
```
Chunk对象只由字节数组构成，序列化、反序列化性能极高，适合集群环境使用redis等缓存服务做缓存的场景。

# 三、实现方式
![整体流程](docs/images/all-steps.png)

字符串形式的表达式在解析器中通过词法分析、语法分析过程得到语法树，然后在分析环节，Loxpression会提取出所有表达式的变量信息，并根据变量间的依赖关系对所有公式做排序，得到可顺序执行的中间表示结构(ExprInfo)。

接下来对表达式的执行，最简单的就是直接解释执行表达式语法树，这种方式适合公式表达式数量比较少的情况，每次都从头解析、分析、执行，性能上也不会有太大问题。但如果每次需要执行的表达式数量都有成千上万条，那么每执行一次都从0开始做解析，就会造成资源的浪费。如果系统是单机环境，那么可以把中间表示结构缓存在内存中。但如果系统是集群部署，缓存是类似redis的独立服务，则中间结构所占的空间就太大了，读写缓存时序列化、反序列化、网络传输会占用很多时间。

针对这种情况，Loxpression提供了字节码格式的执行方式。业务系统在配置好表达式以后，可以先将表达式编译为字节码(Chunk)，然后将字节码放入缓存或者数据库、文件等存储服务中。最后需要执行的时候，从存储/缓存服务中读取出字节码再运行。
## 3.1 解析
### 词法分析
词法分析是Loxpression处理表达式的第一步，目的是把字符串格式的表达式分割成单词(token)列表。我们知道，组成字符串的基本单位是一个一个的字符，但是对于表达式的运行来说，随意截取表达式的一个片段或者任意取子串做分析是没有意义的，比如：
```java
age = currentDate - birthday
```
我们如果关注“currentDate”，就确定这是一个变量，关注“=”或者“-”就知道这是操作符，一个代表赋值一个代表做减法。但如果我们把注意力放在“rrentDa”或者“ge = curr”这样的子串上，那对表达式的分析来说是没有任何意义的。所以词法分析的作用就是把字符串格式的表达式处理为一系列有意义的单词，让编译器后续的处理环节都只关注有意义的单词列表，无需再去分析字符串内部字符间的关系。比如最终有意义的处理结果为：
![词法分析结果](docs/images/tokens.png)
后续处理环节都只以这五个token为最基本的处理单元。

不同的字符所能组合而成的字符串是无限的。但是，token作为构成表达式的基本单位，其类别又是固定的，所有token的类别都在[TokenType.java](src/main/java/com/loxpression/parser/TokenType.java)这个枚举类中定义。词法分析器的作用就是从左往右扫描字符串，并把字符串中的单词归入对应的类别，同时创建出token。代码层面只需对以下几种情况分类处理即可。
- 单字符符号，扫描到的字符只可能是单字符符号，如()[]{},.;-+/%等等，则直接构造出token对象
- 双字符符号，扫描到的字符如果可能是单字符也可能是双字符的开头，那么再往前取一个字符做判断，看是否能构成对应的双字符token，如：!=、==、>=、<=、//、**等。
- 空白，直接跳过，包括空格、回车换行、tab等，注释也直接跳过。
- 字符串字面量，扫描到双引号时，继续往后扫描，直到另一个双引号出现，这中间的部分构成一个字符串字面量。
- 数值字面量，扫描到的字符是数字时，继续往后扫描，直至扫描到非数字符号或者到达结尾，中间的内容构成一个数值字面量。
- 标识符，扫描到字母或者下划线开头时，继续往后扫描，后续遇到的字符只要是字母、数字或者下划线就继续扫描，直达不满足或者到达结尾，收集到的内容就组成了一个标识符。
- 关键字，关键字的匹配作为标识符匹配内的一部分来处理，根据关键字优先原则，只要扫描完成的标识符和某个关键字匹配，就构成一个关键字token。
  
完整实现代码参照[Scanner.java](src/main/java/com/loxpression/parser/Scanner.java)

### 语法分析

## 3.2 分析

## 3.3 解释执行

## 3.4 编译

## 3.5 虚拟机运行
