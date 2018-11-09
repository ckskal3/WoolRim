import {Conn} from './connect_db';
import { types } from 'cormo';

export const Bookmark = Conn.model('bookmark', {
  created: { type: types.Date, required: true },
});