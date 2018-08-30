const sampleData = [
  {
    id: 1,
    name: '진달래꽃',
    poet_id: 1,
    content: '나보기가 역겨워 가실때에는....',
    auth_count: 0,
    point: 100,
  },
  {
    id: 2,
    name: '별헤는밤',
    poet_id: 2,
    content: '별이 헤는 밤에는....',
    auth_count: 3,
    point: 50,
  },
]

const getAllPoem = () => {
  return sampleData;
}

const getPoem = (id) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  return sampleData[id];
}

const createPoem = (poem) => {
  sampleData.push(poem);
  return 'success';
}

const updatePoem = (id, poem) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  if (poem.name) {
    sampleData[id].name = poem.name;
  }
  if (poem.poet_id) {
    sampleData[id].poet_id = poem.poet_id;
  }
  if (poem.content) {
    sampleData[id].content = poem.content;
  }
  if (poem.point) {
    sampleData[id].point = poem.point;
  }
  return 'success';
}

const deletePoem = (id) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  sampleData[id] = null;
  return 'success';
}

const poemResolver = {
  Query: {
    getAllPoem,
    getPoem: (obj, { id }) => getPoem(id),
  },
  Mutation: {
    createPoem: (obj, { input }) => createPoem(input),
    updatePoem: (obj, { id, input }) => updatePoem(id, input),
    deletePoem: (obj, { id }) => deletePoem(id),
  }
}

export { poemResolver };