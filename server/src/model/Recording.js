import { Conn } from './connect_db';
import { types } from 'cormo';

export const Recording = Conn.model('recording', {
  created: { type: types.Date, required: true },
  path: { type: String, required: true },
  auth_flag: { type: types.Integer, default_value: 0 },
  // -1: REJECTED, 0: WAITING, 1: ACCEPTED, 2: APPLIED
  duration: { type: types.Number, required: true },
});
