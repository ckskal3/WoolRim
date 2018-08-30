const sampleData = [
  {
    id: 1,
    name: '김소월',
  },
  {
    id: 2,
    name: '윤동주',
  },
]

const getAllPoet = () => {
  return sampleData;
}

const getPoet = (id) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  return sampleData[id];
}

const createPoet = (poet) => {
  sampleData.push(poet);
  return 'success';
}

const updatePoet = (id, poet) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  if (poet.name) {
    sampleData[id].name = poet.name;
  }
  return 'success';
}

const deletePoet = (id) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  sampleData[id] = null;
  return 'success';
}

const poetResolver = {
  Query: {
    getAllPoet,
    getPoet: (obj, { id }) => getPoet(id),
  },
  Mutation: {
    createPoet: (obj, { input }) => createPoet(input),
    updatePoet: (obj, { id, input }) => updatePoet(id, input),
    deletePoet: (obj, { id }) => deletePoet(id),
  }
}

export { poetResolver };