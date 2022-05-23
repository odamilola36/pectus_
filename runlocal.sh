#!/bin/bash

chmod +x ./mvnw

./mvnw clean verify

java -jar target/*.jar