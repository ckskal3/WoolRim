#!/bin/bash
cd ./server
npm run product
cd ../web
npm run product
echo '----------API 서버 시작, 포트: 3000----------'
echo '----------WEB 서버 시작, 포트: 5000----------'
