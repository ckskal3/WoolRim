import { Connection } from 'cormo';

const isProducttion = process.env.NODE_ENV === 'production';

export const Conn = new Connection('mysql', {
  host: isProducttion ? process.env.DB_ADDR : 'localhost',
  port: 3306,
  user: isProducttion ? 'woolrim_admin' : 'root',
  password: isProducttion ? process.env.DB_PASSWORD : 'Tnrms@@852',
  database: isProducttion ? 'woolrim_db' : 'woolrim',
});
