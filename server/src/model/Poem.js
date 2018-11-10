import { Conn } from './connect_db';
import { types } from 'cormo';

export const Poem = Conn.model('poem', {
  name: { type: String, required: true },
  content: { type: types.Text, required: true },
  auth_count: { type: types.Integer, default_value: 0 }, 
  auth_count_woman: { type: types.Integer, default_value: 0 }, 
  auth_count_man: { type: types.Integer, default_value: 0 }, 
  // 현재까지 시에 대한 심사 완료된 녹음 파일 갯수 
  point: { type: types.Integer, required: true, default_value: 0 },
  length: { type: types.Number, required: true }, 
  // 이 시를 녹음 할 수 있는 길이 (상식 적인 길이), 분 단위
});