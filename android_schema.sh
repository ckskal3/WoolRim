#!/bin/bash
apollo-codegen download-schema http://13.125.221.121:3000/graphql ./
git add ./schema.json
git commit -m '안드로이드 스키마 파일 업로드'