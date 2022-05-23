#!/bin/zsh

docker build -t multistagebuild . && docker run -it multistagebuild