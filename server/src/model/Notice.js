import {Connection} from './connect_db';

export const Notice = Connection.model('notice', {
  content: { type: String, required: true },
  date: { type: Date, required: true },
});