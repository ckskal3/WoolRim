import { Conn } from './connect_db';

export const Poet = Conn.model('poet', {
  name: { type: String, required: true },
});
