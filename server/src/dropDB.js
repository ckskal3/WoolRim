import { Connection } from './model/connect_db';
import { User } from './model/User';
import { Poem } from './model/Poem';
import { Poet } from './model/Poet';
import { Recording } from './model/Recording';
import { Notice } from './model/Notice';

Connection.dropAllModels();
console.log('DB 내 모든 테이블 삭제 !!');