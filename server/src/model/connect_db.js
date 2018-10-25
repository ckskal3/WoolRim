import { Connection } from 'cormo';

export const Conn = new Connection('mysql', { 
    host: 'woolrim-db.czpqx7iovvxj.ap-northeast-2.rds.amazonaws.com',
    port: 3306,
    user: 'woolrim_admin',
    password: 'dnfflawoolrim',
    database: 'woolrim_db',
});

// export const Conn = new Connection('mysql', {
//   host: 'localhost',
//   port: 3306,
//   user: 'root',
//   password: 'Tnrms@@852',
//   database: 'woolrim',
// });
