# Scala中的日志记录
***

## slf4j log4j和logback关系

slf4j框架提供了一组统一的接口，屏蔽了各种不同的日志系统的具体实现。而log4j和logback都是日志的具体实现。用简单的图来表示如下：

<img src="/Users/tang/Documents/flow/java日志框架.png">

底层的具体框架实现可以单独使用，但是这样需要了解的框架就很多了，所以建议配合slf4j一起使用。

### slf4j

全称是`The Simple Logging Facade for Java`。slf4j是日志框架的抽象，使用时需要与具体的实现（如log4j、logback等）绑定起来。使用slf4j的好处是：slf4j提供了一组统一的日志接口，在上层可以根据实际应用场景动态调整底层的日志实现框架，大大提高了灵活性，并且使得上层程序员不再需要去了解每种框架的实现与使用。

### log4j与logback

log4j和logback都是开源的日志工具，logback是log4j的改良版本，用以取代log4j。因此建议使用logback。

logback被分为3个组件：logback-core, logback-classic以及logback-access。其中，logback-core是核心，是另外两个组件的基础。logback-classic则实现了slf4j的API(logback原生实现了slf4j的api，不需要适配层)



## slf4j

slf4j是各种日志框架的上层封装，提供了一个统一的入口，下层可以使用诸如`logback`、`log4j`、`java.util.logging`等各种日志框架。

### 配置

```
libraryDependencies += "com.typesafe.scala-logging" % "scala-logging-slf4j_2.10" % "2.1.2"
# 使用`logback`作为底层日志框架
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
```

### 使用

```
// sbt file
scalaVersion := "2.12.7"

val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
val slf4j = "com.typesafe.scala-logging" % "scala-logging-slf4j_2.10" % "2.1.2"

lazy val loggingObjConfig = (project in file("."))
  .settings(
    name := "testObject",
    version := "0.1",
    libraryDependencies ++= Seq(slf4j, logback)
  )

// scala file
import org.slf4j.LoggerFactory

trait DefaultLogger {
  val logger = LoggerFactory.getLogger("scheduler")
}

object testLoggingObj extends App with DefaultLogger {
  logger.info("hello")
}
```


## scala-logging

`scala-logging`在上层封装了`slf4j`，提供了一个更方便、快捷的Logger。(Scala Logging is a convenient and fast logging library wrapping SLF4J.)

### 配置

由于`scala-logging`是对`slf4j`的封装，因此底层使用的日志系统必须要与`slf4j`兼容，比如`logback`。使用`scala-logging`，并且底层日志系统为`logback`的sbt配置为：

```
val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
val scala_logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"

lazy val loggingObjConfig = (project in file("."))
  .settings(
    name := "testObject",
    version := "0.1",
    libraryDependencies ++= Seq(scala_logging, logback)
  )
```

### 使用

`com.typesafe.scalalogging`包下的`Logging`类中封装了一个底层的`slf4j` logger。要创建一个`Logger`，

1. 传递一个`String`作为名字：

	```
	trait DefaultLogger {
		val logger = Logger("scheduler")
	}
	```
2. 传入一个`slf4j` logger实例：

	```
	val logger = Logger(LoggerFactory.getLogger("scheduler"))
	```
3. 传入一个类：

	```
	val logger = Logger(classOf[MyClass])
	```
	
4. using the runtime class wrapped by the implicit class tag parameter

	```
	val logger = Logger[MyClass]
	```
	
scala代码：

```
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

trait DefaultLogger {
  val logger = Logger("scheduler")
  // val logger = Logger(LoggerFactory.getLogger("scheduler"))
}

object testLoggingObj extends App with DefaultLogger {
  logger.info("hello")
}
```

## akka-slf4j

Akka这个实现了Actor原型的模块，也提供了一个基于SLF4J的封装管理模块，能够以异步事件驱动的方式记录日志(传递给另一个Actor，专门处理日志，所以需要在Actor中才能使用)。

### 配置

```
val akka-slf4j = "com.typesafe.akka" %% "akka-slf4j" % "2.5.17"
val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies ++= Seq(akka-slf4j, logback)
```
