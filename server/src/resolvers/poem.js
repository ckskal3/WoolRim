import { Poem, Poet } from '../model';
import { getPoet } from './poet';
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

const getPoem = async (id) => {
  try {
    return await Poem.find(id);
  } catch (err) {
    console.log('getPoem has err : ', err);
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

const createPoem = async (input_list) => {
  try {
    await Poem.createBulk(input_list);
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('createPoem has err : ', err);
    return {
      isSuccess: false,
    }
  }
}

const updatePoem = async (poem_list) => { // 효율성 떨어짐 추후 수정해야함
  try {
    poem_list.map(async poem => {
      if (poem.name) {
        await Poem.find(poem.id).update({ name: poem.name });
      }
      if (poem.poet_id) {
        await Poem.find(poem.id).update({ poet_id: poem.poet_id });
      }
      if (poem.content) {
        await Poem.find(poem.id).update({ content: poem.content });
      }
      if (poem.point) {
        await Poem.find(poem.id).update({ point: poem.point });
      }
      if (poem.length) {
        await Poem.find(poem.id).update({ length: poem.length });
      }
    })
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('updatePoem has err : ', err);
    return {
      isSuccess: false,
    };
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

const deletePoem = async (id_list) => {
  try {
    await Poem.delete({ id: id_list });
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('deletePoem has err : ', err)
    return {
      isSuccess: false,
    };
  }
}

const poemResolver = {
  Poem: {
    poet: (obj) => getPoet(obj.poet_id),
  },
  Query: {
    getAllPoem: () => getAllPoem(),
    getPoem: (obj, { id }) => getPoem(id),
    getPoemByNames: (obj, { poet_name, poem_name }) => getPoemByNames(poet_name, poem_name),
  },
  Mutation: {
    createPoem: (obj, { input_list }) => createPoem(input_list),
    updateAuthCount: (obj, { poem_id, gender, count }) => updateAuthCount(poem_id, gender, count),
    updatePoem: (obj, { input_list }) => updatePoem(input_list),
    deletePoem: (obj, { id_list }) => deletePoem(id_list),
  }
}

export { poemResolver, getPoem };