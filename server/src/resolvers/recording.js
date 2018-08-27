const sampleData = [
  {
    id: 1,
    path: 'storage/record/',
    auth_flag: 0,
    user_id: 1,
    duration: '5:00',
    poem_id: 3,
  },
  {
    id: 2,
    path: 'storage/record/',
    auth_flag: 1,
    user_id: 2,
    duration: '3:50',
    poem_id: 2,
  },
]

const getAllRecording = () => {
  return sampleData;
}

const getRecording = (id) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  return sampleData[id];
}

const createRecording = (recording) => {
  sampleData.push(recording);
  return 'success';
}

const deleteRecording = (id) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  sampleData[id] = null;
  return 'success';
}

const recordingResolver = {
  Query: {
    getAllRecording,
    getRecording,
  },
  Mutation: {
    createRecording,
    deleteRecording,
  }
}

export { recordingResolver };