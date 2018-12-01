import {Conn} from './connect_db';
import { types } from 'cormo';

export const User = Conn.model('user', {
  name: { type: String, required: true },
  univ: { type: String, required: true },
  admin: { type: types.Boolean, default_value: false},
  stu_id: { type: types.Integer, required: true },
  gender: { type: String, required: true },
  passwd: { type: String, required: true },
  created: { type: Date, required: true },
  bongsa_time: { type: types.Integer, default_value: 0 }, // 기본 값 : 0
  profile: {type: String },
});
