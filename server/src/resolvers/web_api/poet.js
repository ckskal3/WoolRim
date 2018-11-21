import { Poet } from '../../model';

const getAllPoet = async () => {
  return await Poet.query();
}

const getPoet = async (id) => {
  return await Poet.find(id);
}

const createPoet = async (input) => {
  await Poet.create(input);
  return true;
}

const updatePoet = async (poet) => {
  await Poet.find(poet.id).update({ name: poet.name });
  return true;
}

const deletePoet = async (id) => {
  await Poet.delete({ id });
  return true;
}

const poetWebResolver = {
  Query: {
    getAllPoet: () => getAllPoet(),
    getPoet: (obj, { id }) => getPoet(id),
  },
  Mutation: {
    createPoet: (obj, { input }) => createPoet(input),
    updatePoet: (obj, { input }) => updatePoet(input),
    deletePoet: (obj, { id }) => deletePoet(id),
  }
}

export { poetWebResolver, getPoet };