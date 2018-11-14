#!/bin/bash
apollo-codegen download-schema http://52.79.33.194:3000/graphql ./
mv ./schema.json ./client/app/src/main/graphql/org.woolrim.woolrim/
git add ./client/app/src/main/graphql/org.woolrim.woolrim/schema.json
git commit -m '스키마 안드로이드 적용'
git push origin master