import { User } from './model/User';
import { Poem } from './model/Poem';
import { Poet } from './model/Poet';
import { Recording } from './model/Recording';
import { Notice } from './model/Notice';
import { Bookmark } from './model/Bookmark';
import { Notification } from './model/Notification';


console.log('DB 내 모든 테이블 삭제 !!');

User.drop(() => {
  console.log('User Table Drop !!');
})
Poem.drop(() => {
  console.log('Poem Table Drop !!');
})
Poet.drop(() => {
  console.log('Poet Table Drop !!');
})
Recording.drop(() => {
  console.log('Recording Table Drop !!');
})
Notice.drop(() => {
  console.log('Notice Table Drop !!');
})
Bookmark.drop(() => {
  console.log('Bookmark Table Drop !!');
})
Notification.drop(() => {
  console.log('Notification Table Drop !!');
})



