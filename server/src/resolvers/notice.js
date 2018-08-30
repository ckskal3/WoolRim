const sampleData = [
  {
    id: 1,
    content: '공지합니다 1111',
    date: '오늘',
    writer_id: 999,
  },
  {
    id: 2,
    content: '공지합니다 2222',
    date: '어제',
    writer_id: 999,
  },
]

const getAllNotice = () => {
  return sampleData;
}

const getNotice = (id) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  return sampleData[id];
}

const createNotice = (notice) => {
  sampleData.push(notice);
  return 'success';
}

const updateNotice = (id, notice) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  if (notice.content) {
    sampleData[id].content = notice.content;
  }
  if (notice.writer_id) {
    sampleData[id].writer_id = notice.writer_id;
  }
  return 'success';
}

const deleteNotice = (id) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  sampleData[id] = null;
  return 'success';
}

const noticeResolver = {
  Query: {
    getAllNotice,
    getNotice: (obj, { id }) => getNotice(id),
  },
  Mutation: {
    createNotice: (obj, { input }) => createNotice(input),
    updateNotice: (obj, { id, input }) => updateNotice(id, input),
    deleteNotice: (obj, { id }) => deleteNotice(id),
  }
}

export { noticeResolver };