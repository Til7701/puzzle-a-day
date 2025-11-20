#!/bin/sh

java \
  -XX:+UnlockDiagnosticVMOptions \
  -XX:+UnlockExperimentalVMOptions \
  -XX:+LogCompilation \
  -XX:+PrintAssembly \
  -XX:+PrintInlining \
  -XX:LogFile=my_compilation.log \
  -XX:+UseCompactObjectHeaders \
  src/main/java/de/holube/pad/StaticLongArrayMain.java
