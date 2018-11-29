import { Poem } from '../../model';
import { getPoet } from './poet';

const getPoem = async (id) => {
  return await Poem.find(id);
}

const createPoem = async (input) => {
  await Poem.create(input);
  return true;
}

const updatePoem = async (poem) => {
  await Poem.find(poem.id).update({
    name: poem.name,
    content: poem.content,
    point: poem.point,
    length: poem.length,
  });
  return true;
}

const deletePoem = async (id) => {
  await Poem.find(id).delete();
  return true;
}

const poemWebResolver = {
  Poem: {
    poet: (obj) => getPoet(obj.poet_id),
  },
  Query: {
    getPoem: (obj, { id }) => getPoem(id),
  },
  Mutation: {
    createPoem: (obj, { input }) => createPoem(input),
    updatePoem: (obj, { input }) => updatePoem(input),
    deletePoem: (obj, { id }) => deletePoem(id),
  }
}

export { poemWebResolver, getPoem };