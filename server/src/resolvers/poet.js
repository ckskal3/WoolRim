import { Poet } from '../model';

const getAllPoet = async () => {
  try {
    return await Poet.query();
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const getPoet = async (id) => {
  try {
    return await Poet.find(id);
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const createPoet = async (input_poet) => {
  const poet = new Poet({
    name: input_poet.name,
  });
  try {
    await poet.save();
    return 'success';
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const updatePoet = async (id, poet) => {
  try {
    if (poet.name) {
      await Poet.find(id).update({ name: poet.name });
    }
    return 'success';
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const deletePoet = async (id) => {
  try {
    await Poet.delete({ id });
    return 'success';
  } catch (err) {
    console.log(err);
    return 'error';
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