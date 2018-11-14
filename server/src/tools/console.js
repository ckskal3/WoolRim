import { Poem } from '../model';

Poem.update({auth_count_man: 0, auth_count_woman: 0});
console.log('시 테이블 기본값 설정');
