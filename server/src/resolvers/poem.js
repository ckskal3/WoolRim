import { Poem } from '../model';
import { getPoet } from './poet';
/* TODO 
    중복 체크
    poet id 없는 경우 테스트 해보기
*/
const getAllPoem = async () => {
  try {
    return await Poem.query();
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

const createPoem = async (input_poem) => {
  const poem = new Poem({
    name: input_poem.name,
    poet_id: input_poem.poet_id,
    content: input_poem.content,
    point: input_poem.point,
    auth_count: input_poem.auth_count ? input_poem.auth_count : 0,
    length: input_poem.length,
  });
  try {
    await poem.save();
    return {
      item: poem,
      isSuccess: true,
      msg: '시 생성 완료',
    };
  } catch (err) {
    console.log('createPoem has err : ', err);
    return {
      isSuccess: false,
      msg: err,
    }
  }
}

const updatePoem = async (id, poem) => {
  try {
    if (poem.name) {
      await Poem.find(id).update({ name: poem.name });
    }
    if (poem.poet_id) {
      await Poem.find(id).update({ poet_id: poem.poet_id });
    }
    if (poem.content) {
      await Poem.find(id).update({ content: poem.content });
    }
    if (poem.point) {
      await Poem.find(id).update({ point: poem.point });
    }
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('updatePoem has err : ', err);
    return {
      isSuccess: false,
      msg: err,
    };
  }
}

const deletePoem = async (id) => {
  try {
    await Poem.delete({ id });
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('deletePoem has err : ', err)
    return {
      isSuccess: false,
      msg: err,
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
  },
  Mutation: {
    createPoem: (obj, { input }) => createPoem(input),
    updatePoem: (obj, { id, input }) => updatePoem(id, input),
    deletePoem: (obj, { id }) => deletePoem(id),
  }
}

export { poemResolver, getPoem };