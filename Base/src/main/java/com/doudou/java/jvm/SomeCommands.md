# 常用java命令
**以下命令都是基于JDK12进行测试说明的**
## 1.jinfo

jinfo主要用啦查看虚拟机的配置信息和系统配置（环境变量）
```text
Usage:
    jinfo <option> <pid>
       (to connect to a running process)

where <option> is one of:
    -flag <name>         to print the value of the named VM flag
    -flag [+|-]<name>    to enable or disable the named VM flag
    -flag <name>=<value> to set the named VM flag to the given value
    -flags               to print VM flags
    -sysprops            to print Java system properties
    <no option>          to print both VM flags and system properties
    -? | -h | --help | -help to print this help message
```

注意：

```text
-Xmx ：jvm的最大值  -XX:MaxHeapSize 的简写  
-Xms ：jvm的最小值  -XX:InitialHeapSize 的简写  
-Xss               -XX:ThreadStackSize 的简写,Stack 栈 
```
 

## 2.jmap
jmap主要用于生成堆的dump文件， 堆内存的一些信息
```text
Usage:
    jmap -clstats <pid>
        to connect to running process and print class loader statistics
    jmap -finalizerinfo <pid>
        to connect to running process and print information on objects awaiting finalization
    jmap -histo[:live] <pid>
        to connect to running process and print histogram of java object heap
        if the "live" suboption is specified, only count live objects
    jmap -dump:<dump-options> <pid>
        to connect to running process and dump java heap
    jmap -? -h --help
        to print this help message

    dump-options:
      live         dump only live objects; if not specified,
                   all objects in the heap are dumped.
      format=b     binary format
      file=<file>  dump heap to <file>

    Example: jmap -dump:live,format=b,file=heap.bin <pid>
```
示例：
```text
C:\Users\dream>jmap -histo 1152
 num     #instances         #bytes  class name (module)
-------------------------------------------------------
   1:         20220        8952360  [B (java.base@12.0.1)
   2:          1426        1352064  [I (java.base@12.0.1)
   3:         19222         461328  java.lang.String (java.base@12.0.1)
   4:          4006         446624  java.lang.Class (java.base@12.0.1)
```

## 3.jstack

jstack用来生成当前时刻虚拟机的线程快照

```text
Usage:
    jstack [-l][-e] <pid>
        (to connect to running process)

Options:
    -l  long listing. Prints additional information about locks
    -e  extended listing. Prints additional information about threads
    -? -h --help -help to print this help message
```

## 4. jhat

## 5. jcmd

