import express from 'express';
import graphqlHTTP from 'express-graphql';
import schema from './graphql/index';
import cors from 'cors';
import fs from 'fs';
import path from 'path';

const multer = require('multer');

const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    const dir = path.join(__dirname,`../../../woolrim_storage/${req.body.stu_id}/`)
    if(!fs.existsSync(dir)){
      fs.mkdirSync(dir);
    }
    cb(null, dir)
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname)
  }
})

const upload = multer({ storage: storage });

const apiServer = express();
const fileServer = express();

fileServer.use(cors());
fileServer.set('view engine', 'jade')
fileServer.set('views', __dirname + '/templates')

apiServer.use(cors());

apiServer.get('/graphql', graphqlHTTP({
  schema,
  graphiql: true
}));

apiServer.post('/graphql', graphqlHTTP({
  schema,
  graphiql: false
}));

apiServer.get('/', (req, res, next) => {
  res.send('hello im woolrim');
});

apiServer.listen(3000, () => {
  console.log('3000번 api 서버 포트 개방!!');
});

fileServer.post('/upload', upload.single('user_recording'), function (req, res) {
  res.status(200).send('success');
});

fileServer.get('/upload', function (req, res) {
  res.render('upload');
});

fileServer.listen(4000, () => {
  console.log('4000번 file 서버 포트 개방!!');
});
