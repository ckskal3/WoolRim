import {Connection} from './connect_db';
import cormo from 'cormo';

export const User = Connection.model('user', {
  name: { type: String, required: true },
  type: { type: String, required: true }, // 인천대생 : INCHEON (1) , 일반인 : GENERAL (0) 
  stu_id: { type: cormo.types.Integer }, // 인천대생만 필요
  gender: { type: String, required: true },
  passwd: { type: String, required: true },
  created: { type: Date, required: true },
  bongsa_time: { type: cormo.types.Integer }, // 기본 값 : 0
});
