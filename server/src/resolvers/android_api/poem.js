import { Poem, Poet } from '../../model';
import { getPoet } from '../web_api/poet';
/* TODO 
    poet id 없는 경우 테스트 해보기 -> err 메세지 클라이언트로 던져주는 방법 알아보기
*/
const getAllPoem = async () => {
  try {
    const result = await Poem.query().order('name');
    return result;
  } catch (err) {
    console.log('getAllPoem has err : ', err);
    return null;
  }
}

const getPoemByNames = async (poet_name, poem_name) => {
  try {
    const poet_ids = (await Poet.where({name: poet_name}).select('id')).map(v => {
      return {poet_id: v.id};
    });
    const result = await Poem.where({$or: poet_ids}).where({name: poem_name}).one();
    return result;
  } catch (err) {
    console.log('getPoem has err : ', err);
    return null;
  }
}

const updateAuthCount = async (poem_id, gender, count) => {
  if(!(gender === '남자' || gender === '여자')){
    return false;
  }
  if(gender === '남자'){
    await Poem.where({id: poem_id}).update({auth_count_man: count});
  }else{
    await Poem.where({id: poem_id}).update({auth_count_woman: count});
  }
  return true;
}

const poemResolver = {
  Poem: {
    poet: (obj) => getPoet(obj.poet_id),
  },
  Query: {
    getAllPoem: () => getAllPoem(),
    getPoemByNames: (obj, { poet_name, poem_name }) => getPoemByNames(poet_name, poem_name),
  },
  Mutation: {
    updateAuthCount: (obj, { poem_id, gender, count }) => updateAuthCount(poem_id, gender, count),
  }
}

export { poemResolver };