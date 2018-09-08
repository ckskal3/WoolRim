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

const createPoet = async (input_poet) => {
  const poet = new Poet({
    name: input_poet.name,
  });
  try {
    await poet.save();
    return {
      item: poet,
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

const deletePoet = async (id) => {
  try {
    await Poet.delete({ id });
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
    getAllPoet: () => getAllPoet(id),
    getPoet: (obj, { id }) => getPoet(id),
  },
  Mutation: {
    createPoet: (obj, { input }) => createPoet(input),
    updatePoet: (obj, { id, input }) => updatePoet(id, input),
    deletePoet: (obj, { id }) => deletePoet(id),
  }
}

export { poetResolver };