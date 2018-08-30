import {Connection} from './connect_db';
import cormo from 'cormo';

export const User = Connection.model('user', {
  name: { type: String, required: true },
  stu_id: { type: cormo.types.Integer, required: true },
  gender: { type: String, required: true },
  passwd: { type: String, required: true },
  created: { type: Date, required: true },
  bongsa_time: { type: cormo.types.Integer },
});
