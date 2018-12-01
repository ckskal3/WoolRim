import express from 'express';
import graphqlHTTP from 'express-graphql';
import schema from './graphql/index';
import cors from 'cors';
import fs from 'fs';
import path from 'path';
import mixAudio from './mixAudio';

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

fileServer.use(express.static(path.join(__dirname,'../../../woolrim_storage/')));
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

fileServer.post('/upload', upload.single('user_recording'), async function (req, res) {
  // console.log(req.body); // stu id
  // console.log(req.file)
  // { fieldname: 'user_recording',
  // originalname: 'AudioTest.aac',
  // encoding: '7bit',
  // mimetype: 'audio/aac',
  // destination: '/Users/jaws/privateProj/woolrim_storage/555555/',
  // filename: 'AudioTest.aac',
  // path: '/Users/jaws/privateProj/woolrim_storage/555555/AudioTest.aac',
  // size: 571370 }
  
  console.time('mix_time')
  if(await mixAudio(req.body.stu_id, req.file.originalname)){
    console.timeEnd('mix_time')
    res.status(200).send('success');
    console.log('학번: ',req.body.stu_id,', 파일이름: ',req.file.originalname,' 합성 성공');
    const file_name_without_type = req.file.filename.replace(/\..+$/, '').trim();
    fs.renameSync(req.file.path, `${req.file.destination}${file_name_without_type}_0.mp3`);
  }else{
    console.timeEnd('mix_time')
    res.status(500).send('fail');
    console.log('학번: ',req.body.stu_id,', 파일이름: ',req.file.originalname,' 합성 실패');
  }
});

fileServer.get('/upload', function (req, res) {
  res.render('upload');
});

fileServer.get('/:id/:filename' ,function(req, res) {
  res.sendFile(path.join(__dirname,`../../../woolrim_storage/${req.params.id}/${req.params.filename}`))
  // res.sendFile(path.join(__dirname,`../uploads/${req.params.id}/${req.params.filename}`))
})

fileServer.listen(4000, () => {
  console.log('4000번 file 서버 포트 개방!!');
});
