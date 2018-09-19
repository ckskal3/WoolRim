import { User } from '../model';

const getAllUser = async () => {
  try {
    return await User.query();
  } catch (err) {
    console.log('getAllUser has err : ', err);
    return null;
  }
}

const getUser = async (id) => {
  try {
    return await User.find(id);
  } catch (err) {
    console.log('getUser has err : ', err);
    return null;
  }
}

const createUser = async (input_user) => {
  const user = new User({
    name: input_user.name,
    stu_id: input_user.stu_id,
    gender: input_user.gender,
    passwd: input_user.passwd,
    created: new Date(),
    bongsa_time: input_user.bongsa_time ? input_user.bongsa_time : 0,
  })
  try {
    await user.save();
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('createUser has err : ', err);
    return {
      isSuccess: false,
      msg: err,
    };
  }
}

const updateUser = async (id, user) => {
  console.log(`id : ${id}, user : ${user}`)
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
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('updateUser has err : ', err);
    return {
      isSuccess: false,
      msg: err,
    };
  }
}

const deleteUser = async (id) => {
  try {
    await User.delete({ id });
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('deleteUser has err : ', err);
    return {
      isSuccess: false,
      msg: err,
    };
  }
}

const userResolver = {
  Query: {
    getAllUser: () => getAllUser(),
    getUser: (obj, { id }) => getUser(id),
  },
  Mutation: {
    createUser: (obj, { input }) => createUser(input),
    updateUser: (obj, { id, input }) => updateUser(id, input),
    deleteUser: (obj, { id }) => deleteUser(id),
  }
}

export { userResolver };