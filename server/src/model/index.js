import { Conn } from './connect_db';
import { User } from './User';
import { Poem } from './Poem';
import { Poet } from './Poet';
import { Recording } from './Recording';
import { Notice } from './Notice';
import { Notification } from './Notification'


User.hasMany(Recording, { integrity: 'delete' });
Recording.belongsTo(User, { required: true });

User.hasMany(Notice, { integrity: 'delete' });
Notice.belongsTo(User, { required: true });

User.hasMany(Notification, { integrity: 'delete' });
Notification.belongsTo(User, { required: true });

Poem.hasMany(Recording, { integrity: 'delete' });
Recording.belongsTo(Poem, { required: true });

Poet.hasMany(Poem, { integrity: 'delete' });
Poem.belongsTo(Poet, { required: true });

Conn.applySchemas({ verbose: true });

export { Conn };
export * from './User';
export * from './Poet';
export * from './Poem';
export * from './Notice';
export * from './Recording';
export * from './Notification';
