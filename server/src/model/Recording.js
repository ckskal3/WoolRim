import { Conn } from './connect_db';
import { types } from 'cormo';

export const Recording = Conn.model('recording', {
  path: { type: String, required: true },
  auth_flag: { type: types.Integer, default_value: 0 },
  // 0: WAITING, 1: ACCEPTED, -1: REJECTED
  duration: { type: types.Number, required: true },
});
