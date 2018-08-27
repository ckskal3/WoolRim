const sampleData = [
  {
    id: 1,
    name: 'tnrms',
    stu_id: 201201548,
    gender: '남',
    passwd: '123445',
    created: '오늘',
    bongsa_time: 30,
  },
  {
    id: 2,
    name: 'ckskal',
    stu_id: 201201547,
    gender: '남',
    passwd: '123456',
    created: 'tomorrow',
    bongsa_time: 50,
  },
]

const getAllUser = () => {
  return sampleData;
}

const getUser = (id) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  return sampleData[id];
}

const createUser = (user) => {
  sampleData.push(user);
  return 'success';
}

const updateUser = (id, user) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  if (user.name) {
    sampleData[id].name = user.name;
  }
  if (user.stu_id) {
    sampleData[id].stu_id = user.stu_id;
  }
  if (user.gender) {
    sampleData[id].gender = user.gender;
  }
  if (user.passwd) {
    sampleData[id].passwd = user.passwd;
  }
  if (user.bongsa_time) {
    sampleData[id].bongsa_time = user.bongsa_time;
  }
  return 'success';
}

const deleteUser = (id) => {
  if (id >= sampleData.length || id < 0) {
    return '범위 벗어남'
  }
  sampleData[id] = null;
  return 'success';
}

const userResolver = {
  Query: {
    getAllUser,
    getUser: (obj, { id }) => getUser(id),
  },
  Mutation: {
    createUser,
    updateUser,
    deleteUser,
  }
}

export { userResolver };