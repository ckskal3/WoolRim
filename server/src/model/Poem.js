import {Connection} from './connect_db';
import cormo from 'cormo';

export const Poem = Connection.model('poem', {
  name: { type: String, required: true },
  content: { type: String, required: true },
  auth_count: { type: cormo.types.Integer, required: true },
  point: { type: cormo.types.Integer, required: true },
});