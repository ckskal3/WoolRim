const express = require('express');
const path = require('path');

const app = express();

app.use(express.static(path.join(__dirname, 'build')))

app.listen(5000, function (req, res) {
  console.log('5000 포트 개방!')
  console.log(path.join(__dirname, 'build'));
});