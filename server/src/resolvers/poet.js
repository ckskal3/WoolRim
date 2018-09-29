import { Poet } from '../model';

const getAllPoet = async () => {
  try {
    return await Poet.query();
  } catch (err) {
    console.log('getAllPoet has err : ', err);
    return null;
  }
}

const getPoet = async (id) => {
  try {
    return await Poet.find(id);
  } catch (err) {
    console.log('getPoet has err : ', err);
    return null;
  }
}

const createPoet = async (input_list) => {
  try {
    await Poet.createBulk(input_list);
    return {
      isSuccess: true,
      msg: '시인 생성 완료',
    };
  } catch (err) {
    console.log('createPoet has err : ', err);
    return {
      isSuccess: false,
      msg: err,
    };
  }
}

const updatePoet = async (id, poet) => {
  try {
    if (poet.name) {
      await Poet.find(id).update({ name: poet.name });
    }
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('updatePoet has err : ', err);
    return {
      isSuccess: false,
      msg: err,
    };
  }
}

const deletePoet = async (id_list) => {
  try {
    await Poet.delete({ id: id_list });
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('deletePoet has err : ', err);
    return {
      isSuccess: false,
      msg: err,
    };
  }
}

const poetResolver = {
  Query: {
    getAllPoet: () => getAllPoet(),
    getPoet: (obj, { id }) => getPoet(id),
  },
  Mutation: {
    createPoet: (obj, { input_list }) => createPoet(input_list),
    updatePoet: (obj, { id, input }) => updatePoet(id, input),
    deletePoet: (obj, { id_list }) => deletePoet(id_list),
  }
}

export { poetResolver, getPoet };