import {Conn} from './connect_db';
import { types } from 'cormo';

export const Notice = Conn.model('notice', {
  content: { type: types.Text, required: true },
  created: { type: Date, required: true },
});