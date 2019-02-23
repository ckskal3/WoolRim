import express from 'express';
import graphqlHTTP from 'express-graphql';
import schema from './graphql/index';
import cors from 'cors';
import fs from 'fs';
import path from 'path';
import mixAudio from './mixAudio';
import bodyParser from 'body-parser';
import http from 'http';

const multer = require('multer');

const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    const dir = path.join(__dirname, `../../../woolrim_storage/${req.body.stu_id}/`)
    if (!fs.existsSync(dir)) {
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

fileServer.use(bodyParser.json());
fileServer.use(bodyParser.urlencoded({ extended: true }));
fileServer.use(express.static(path.join(__dirname, '../../../woolrim_storage/')));
fileServer.use(cors());
fileServer.set('view engine', 'jade')
fileServer.set('views', __dirname + '/templates')

apiServer.use(cors());

apiServer.post('/graphql', graphqlHTTP({
  schema,
  graphiql: false,
}));

apiServer.get('/', (req, res, next) => {
  res.send('hello im woolrim');
});

apiServer.listen(3000, () => {
  console.log('api server at port 3000');
});

fileServer.get('/mix_complete', function (req, res) { res.render('mix_complete') })
fileServer.post('/mix_complete', async function (req, res) {
  const file_name_without_type = req.body.file_name.replace(/\..+$/, '').trim();
  const file_path = path.join(__dirname, `../../../woolrim_storage/${req.body.stu_id}/`);
  const background_index = [1, 2, 3];
  const mix_num = Number(req.body.mix_num);
  let recording_path;
  if (mix_num) {
    background_index.filter(v => v !== mix_num).forEach(v => {
      if (fs.existsSync(file_path + file_name_without_type + `_${v}.mp3`)) {
        fs.unlinkSync(file_path + file_name_without_type + `_${v}.mp3`)
      }
    })
    if (fs.existsSync(file_path + file_name_without_type + '.aac')) {
      fs.unlinkSync(file_path + file_name_without_type + '.aac')
    }
    recording_path = `${req.body.stu_id}/${file_name_without_type}_${mix_num}.mp3`;
  } else {
    if (fs.existsSync(file_path + file_name_without_type + '_1.mp3')) {
      fs.unlinkSync(file_path + file_name_without_type + '_1.mp3')
    }
    if (fs.existsSync(file_path + file_name_without_type + '_2.mp3')) {
      fs.unlinkSync(file_path + file_name_without_type + '_2.mp3')
    }
    if (fs.existsSync(file_path + file_name_without_type + '_3.mp3')) {
      fs.unlinkSync(file_path + file_name_without_type + '_3.mp3')
    }
    recording_path = `${req.body.stu_id}/${file_name_without_type}.aac`;
  }
  const result = await createRecording(recording_path, req.body.stu_id, req.body.poem_name, req.body.poet_name, req.body.duration);
  if(result.data.createRecording.isSuccess){
    res.status(200).send('success');
  }else{
    res.status(500).send('fail');
  }
})

const createRecording = (path, stu_id, poem_name, poet_name, duration) => {
  return new Promise(function(resolve, reject){
    const option = {
      port: 3000,
      hostname: '127.0.0.1',
      method: 'POST',
      path: '/graphql',
      headers: {
        'Content-Type': 'application/json'
      },
    };
      const req = http.request(option, (res) => {
        res.on('data', (chunk) => {
          resolve(JSON.parse(chunk));
        });
      });
      const query = `mutation($input: CreateRecordingInput!) {
          createRecording(input: $input){
            isSuccess
          }
        }`
      const variables = {
        input: {
          path,
          duration,
          stu_id,
          poem_name,
          poet_name,
        }
      }
      req.on('error', (e) => {
        reject(e);
      })
      req.write(JSON.stringify({ query, variables }));
      req.end();
  })
}
fileServer.get('/remove_record', function (req, res) { res.render('remove_record') })
fileServer.post('/remove_record', function (req, res) {
  const file_name_without_type = req.body.file_name.replace(/\..+$/, '').trim();
  const file_path = path.join(__dirname, `../../../woolrim_storage/${req.body.stu_id}/`);
  if (fs.existsSync(file_path + file_name_without_type + '.aac')) {
    fs.unlinkSync(file_path + file_name_without_type + '.aac')
  }
  if (fs.existsSync(file_path + file_name_without_type + '_1.mp3')) {
    fs.unlinkSync(file_path + file_name_without_type + '_1.mp3')
  }
  if (fs.existsSync(file_path + file_name_without_type + '_2.mp3')) {
    fs.unlinkSync(file_path + file_name_without_type + '_2.mp3')
  }
  if (fs.existsSync(file_path + file_name_without_type + '_3.mp3')) {
    fs.unlinkSync(file_path + file_name_without_type + '_3.mp3')
  }
  res.status(200).send('success')
});

fileServer.post('/mix', async function (req, res) {
  if (req.body.mix_num) {
    console.time('mix_time')
    if (await mixAudio(req.body.stu_id, req.body.file_name, req.body.mix_num)) {
      console.timeEnd('mix_time')
      res.status(200).send('success');
      console.log('학번: ', req.body.stu_id, ', 파일이름: ', req.body.file_name, ' 합성 성공');
    } else {
      console.timeEnd('mix_time')
      res.status(500).send('mix_fail');
      console.log('학번: ', req.body.stu_id, ', 파일이름: ', req.body.file_name, ' 합성 실패');
    }
  } else {
    const file_name_without_type = req.body.file_name.replace(/\..+$/, '').trim();
    const path = path.join(__dirname, `../../../woolrim_storage/${req.body.stu_id}/${file_name_without_type}_0.mp3`)
    if (fs.existsSync(path)) {
      res.status(200).send('success');
    } else {
      res.status(404).send('file_not_found')
    }
  }
});

fileServer.post('/upload', upload.single('user_recording'), async function (req, res) {
  if (fs.existsSync(req.file.path)) {
    res.status(200).send('success');
  } else {
    res.status(500).send('fail');
  }
});

fileServer.get('/upload', function (req, res) {
  res.render('upload');
});

fileServer.get('/mix', function (req, res) {
  res.render('mix');
});

fileServer.get('/:id/:filename', function (req, res) {
  res.sendFile(path.join(__dirname, `../../../woolrim_storage/${req.params.id}/${req.params.filename}`))
})

fileServer.listen(4000, () => {
  console.log('file server at port 4000');
});
