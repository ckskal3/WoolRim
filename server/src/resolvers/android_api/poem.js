import { Poem, Poet } from '../../model';
import { getPoet } from '../web_api/poet';
/* TODO 
    poet id 없는 경우 테스트 해보기 -> err 메세지 클라이언트로 던져주는 방법 알아보기
*/
const getAllPoem = async () => {
  try {
    const poet_list = await Poet.query().order('name');
    const result = await Promise.all(poet_list.map(async (poet) => {
      const poem_list = await Poem.where({poet_id: poet.id}).order('name');
      return {
        poet,
        poem_list,
      }
    }));
    return result;
  } catch (err) {
    console.log('getAllPoem has err : ', err);
    return [];
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

const poemResolver = {
  Poem: {
    poet: (obj) => getPoet(obj.poet_id),
  },
  Query: {
    getAllPoem: () => getAllPoem(),
    getAllPoemForWeb: () => Poem.query(),
    getPoemByNames: (obj, { poet_name, poem_name }) => getPoemByNames(poet_name, poem_name),
  },
}

export { poemResolver };