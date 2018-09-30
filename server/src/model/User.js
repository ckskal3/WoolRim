import {Conn} from './connect_db';
import { types } from 'cormo';

export const User = Conn.model('user', {
  name: { type: String, required: true },
  type: { type: String, required: true }, // 인천대생 : INCHEON (1) , 일반인 : GENERAL (0) 
  stu_id: { type: types.Integer }, // 인천대생만 필요
  gender: { type: String, required: true },
  passwd: { type: String, required: true },
  created: { type: Date, required: true },
  bongsa_time: { type: types.Integer }, // 기본 값 : 0
});
