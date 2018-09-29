import {Connection} from './connect_db';
import cormo from 'cormo';

export const Poem = Connection.model('poem', {
  name: { type: String, required: true },
  content: { type: String, required: true },
  auth_count: { type: cormo.types.Integer, required: true}, // 서버에서 기본 값 0 으로 설정
  point: { type: cormo.types.Integer, required: true },
  length: { type: cormo.types.Integer, required: true }, // 시 길이 (상식 적인 길이), 분 단위
});