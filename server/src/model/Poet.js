import {Connection} from './connect_db';
 
export const Poet = Connection.model('poet', {
  name: { type: String },
});
