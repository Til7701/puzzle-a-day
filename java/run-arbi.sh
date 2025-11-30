#!/bin/sh

# time based log file name
LOG_FILE_NAME=arbi-log-$(date +%Y%m%d-%H%M%S).log

java \
  --enable-preview \
  src/main/java/de/holube/pad/StaticLongArrayMain.java >> $LOG_FILE_NAME
