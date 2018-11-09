import {Conn} from './connect_db';
import { types } from 'cormo';

export const Notification = Conn.model('notification', {
  content: { type: types.String, required: true },
  created: { type: Date, required: true },
  read_flag: { type: types.Boolean, default_value: false},
});