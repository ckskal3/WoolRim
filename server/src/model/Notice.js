import {Conn} from './connect_db';

export const Notice = Conn.model('notice', {
  content: { type: String, required: true },
  date_created: { type: Date, required: true },
});