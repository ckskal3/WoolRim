import express from 'express';
import graphqlHTTP from 'express-graphql';
import schema from './graphql/index';
import path from 'path';

const multer = require('multer'); // express에 multer모듈 적용 (for 파일업로드)
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    console.log(req.body)
    cb(null, 'uploads/') // cb 콜백함수를 통해 전송된 파일 저장 디렉토리 설정
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname) // cb 콜백함수를 통해 전송된 파일 이름 설정
  }
})
const bodyParser = require('body-parser')

const upload = multer({ storage: storage })

const app = express();
const webApp = express();

webApp.listen(80, () => {
  console.log('웹 서버용 80 포트 개방!');
})

webApp.use(express.static(path.join(__dirname,'/../../web/build')));

app.set('view engine', 'jade')
app.set('views', __dirname + '/templates')
app.use( bodyParser.json() );       // to support JSON-encoded bodies
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
  extended: true
})); 
app.get('/graphql', graphqlHTTP({
  schema,
  graphiql:true
}));

app.post('/graphql', graphqlHTTP({
  schema,
  graphiql:false
}));

app.get('/upload', function(req, res){
  res.render('upload');
});

app.post('/upload', upload.single('file'), function(req, res){
  res.status(200).send('Uploaded! : '+req.file.originalname); // object를 리턴함
  console.log(req.body)
});

app.get('/', (req, res, next) => {
  res.send('hello im woolrim');
});

app.listen(3000,  () => {
  console.log('3000번 포트 개방!!');
});
