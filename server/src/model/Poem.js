import { Conn } from './connect_db';
import { types } from 'cormo';

export const Poem = Conn.model('poem', {
  name: { type: String, required: true },
  content: { type: String, required: true },
  auth_count: { type: types.Integer, required: true }, // 서버에서 기본 값 0 으로 설정
  point: { type: types.Integer, required: true },
  length: { type: types.Integer, required: true }, // 시 길이 (상식 적인 길이), 분 단위
});