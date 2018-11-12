import { Poem, Poet } from '../model';
import { getPoet } from './poet';
const getAllPoem = async () => {
  return await Poem.query();
}

const getPoem = async (id) => {
  return await Poem.find(id);
}

const createPoem = async (input) => {
  await Poem.create(input);
  return true;
}

const updatePoem = async (poem) => {
  await Poem.find(poem.id).update(...poem);
  return true;
}

const deletePoem = async (id) => {
  await Poem.delete({ id });
  return true;
}

const poemWebResolver = {
  Poem: {
    poet: (obj) => getPoet(obj.poet_id),
  },
  Query: {
    getAllPoem: () => getAllPoem(),
    getPoem: (obj, { id }) => getPoem(id),
  },
  Mutation: {
    createPoem: (obj, { input }) => createPoem(input),
    updatePoem: (obj, { input }) => updatePoem(input),
    deletePoem: (obj, { id }) => deletePoem(id),
  }
}

export { poemWebResolver, getPoem };