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
  console.log('backoffice web server at port 5000')
});

app.post('/login', async (req, res) => {
  const query = `mutation($stu_id: Int!, $passwd: String!) {
    adminLogin(stu_id: $stu_id, passwd: $passwd)
  }`
  const result = (await axios.post(serverInfo.serverURL, {
    query,
    variables: {
      stu_id: req.body.id,
      passwd: req.body.passwd,
    }
  })).data.data.adminLogin;
  res.json(result);
})