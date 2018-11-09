import {Conn} from './connect_db';

export const Notice = Conn.model('notice', {
  content: { type: String, required: true },
  created: { type: Date, required: true },
});