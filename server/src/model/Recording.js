import {Connection} from './connect_db';

export const Recording = Connection.model('recording', {
  path: { type: String, required: true },
  auth_flag: { type: Boolean, required: true },
  duration: { type: String, required: true },
});
