#!/bin/sh

java \
  -XX:+UseCompactObjectHeaders \
  src/main/java/de/holube/pad/StaticLongArrayMain.java
