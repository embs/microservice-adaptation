#!/bin/bash

cat $1 | jq '.message?'| sed 's/\\//g' | sed 's/"{/{/g' | sed 's/}"/}/g' | jq '.method?,.time?'| sed 'N;s/\n/ /' | grep workflow | cut -d " " -f 2 > $2
