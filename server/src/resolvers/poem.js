import { Poem } from '../model';

const getAllPoem = async () => {
  try {
    return await Poem.query();
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const getPoem = async (id) => {
  try {
    return await Poem.find(id);
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const createPoem = async (input_poem) => {
  const poem = new Poem({
    name: input_poem.name,
    poet_id: input_poem.poet_id,
    content: input_poem.content,
    point: input_poem.point,
  });
  try {
    await poem.save();
    return 'success';
  } catch (err) {
    console.log(err);
    return 'error';
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
    return 'success';
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const deletePoem = async (id) => {
  try {
    await Poem.delete({ id });
    return 'success';
  } catch (err) {
    console.log(err)
    return 'error';
  }
}

const poemResolver = {
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

export { poemResolver };