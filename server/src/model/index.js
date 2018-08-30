import {Connection} from './connect_db';
import {User} from './User';
import {Poem} from './Poem';
import {Poet} from './Poet';
import {Recording} from './Recording';
import {Notice} from './Notice';

//Conn.dropAllModels();

User.hasMany(Recording);
Recording.belongsTo(User);

User.hasMany(Notice);
Notice.belongsTo(User);

Poem.hasMany(Recording);
Recording.belongsTo(Poem);

Poet.hasMany(Poem);
Poem.belongsTo(Poet);

Connection.applySchemasSync({ verbose: true });

export { Connection };
export * from './User';
export * from './Poet';
export * from './Poem';
export * from './Notice';
export * from './Recording';
