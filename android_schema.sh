#!/bin/bash
apollo-codegen download-schema http://52.79.33.194:3000/graphql ./
zip schema.json.zip ./schema.json
open .
