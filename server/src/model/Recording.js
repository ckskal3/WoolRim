import { Conn } from './connect_db';
import { types } from 'cormo';

export const Recording = Conn.model('recording', {
  path: { type: String, required: true },
  auth_flag: { type: types.Integer, required: true }, // 기본값은 서버에서 0 (WAITING) 으로 설정
  // 0: WAITING, 1: ACCEPTED, -1: REJECTED
  duration: { type: String, required: true },
});
