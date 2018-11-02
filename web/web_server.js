import express from 'express';
import path from 'path';
import cors from 'cors';
import bodyParser from 'body-parser';
import axios from 'axios';
import serverInfo from './src/serverInfo';

const app = express();
app.use(bodyParser.json());
app.use(cors());
app.use(bodyParser.urlencoded({ extended: false }))

app.use(express.static(path.join(__dirname, 'build')))

app.get('/*', (req, res) => {
  res.sendFile(path.join(__dirname, 'build', 'index.html'));
});

app.listen(5000, (req, res) => {
  console.log('5000 포트 개방!')
  console.log(path.join(__dirname, 'build'));
});

app.post('/login', async (req, res) => {
  const query = `mutation($name: String!, $passwd: String!) {
    adminLogin(name: $name, passwd: $passwd){
      isSuccess
      user {
        id
        name
      }
    }
  }`
  const result = (await axios.post(serverInfo.serverURL, {
    query,
    variables: {
      name: req.body.id,
      passwd: req.body.passwd,
    }
  })).data.data.adminLogin;
  res.json(result);
})