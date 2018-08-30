import { User } from '../model';

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

const getAllUser = async () => {
  return await User.query();
}

const getUser = async (id) => {
  return await User.find(id);
}

const createUser = async (input_user) => {
  const user = new User({
    name: input_user.name,
    stu_id: input_user.stu_id,
    gender: input_user.gender,
    passwd: input_user.passwd,
    created: new Date(),
    bongsa_time: input_user.bongsa_time,
  })
  try {
    await user.save();
    return 'success';
  } catch (err) {
    console.log(err);
    return 'err';
  }
}

const updateUser = async (id, user) => {
  try {
    if (user.name) {
      await User.find(id).update({ name: user.name })
    }
    if (user.stu_id) {
      await User.find(id).update({ stu_id: user.stu_id })
    }
    if (user.gender) {
      await User.find(id).update({ gender: user.gender })
    }
    if (user.passwd) {
      await User.find(id).update({ passwd: user.passwd })
    }
    if (user.bongsa_time) {
      await User.find(id).update({ bongsa_time: user.bongsa_time })
    }
    return 'success';
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const deleteUser = async (id) => {
  try {
    await User.delete({ id });
  } catch (err) {
    console.log(err);
    return 'error';
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
    createUser: (obj, { input }) => createUser(input),
    updateUser: (obj, { id, input }) => updateUser(id, input),
    deleteUser: (obj, { id }) => deleteUser(id),
  }
}

export { userResolver };