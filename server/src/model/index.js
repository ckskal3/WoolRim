import { Conn } from './connect_db';
import { User } from './User';
import { Poem } from './Poem';
import { Poet } from './Poet';
import { Recording } from './Recording';
import { Notice } from './Notice';
import { Notification } from './Notification';
import { Bookmark } from './Bookmark';


User.hasMany(Recording, { integrity: 'nullify' });
Recording.belongsTo(User, { required: true });

User.hasMany(Notice, { integrity: 'nullify' });
Notice.belongsTo(User, { required: true });

User.hasMany(Notification, { integrity: 'delete' });
Notification.belongsTo(User, { required: true });

User.hasMany(Bookmark, { integrity: 'delete' });
Bookmark.belongsTo(User, { required: true });

Poem.hasMany(Recording, { integrity: 'nullify' });
Recording.belongsTo(Poem, { required: true });

Poet.hasMany(Poem, { integrity: 'delete' });
Poem.belongsTo(Poet, { required: true });

Recording.hasMany(Bookmark, { integrity: 'delete' });
Bookmark.belongsTo(Recording);

Conn.applySchemas({ verbose: true });

export { Conn };
export * from './User';
export * from './Poet';
export * from './Poem';
export * from './Notice';
export * from './Recording';
export * from './Notification';
export * from './Bookmark';
