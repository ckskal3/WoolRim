import { User } from '../../model';

const getAllUser = async () => {
  return await User.query();
}

export const getUser = async (id) => {
  return await User.find(id);
}

const updateUser = async (user) => {
  await User.find(user.id).update(...user);
  return true;
}

const deleteUser = async (id) => {
  await User.delete({ id });
  return true;
}

const userWebResolver = {
  Query: {
    getAllUser: () => getAllUser(),
    getUser: (obj, { id }) => getUser(id),
  },
  Mutation: {
    updateUser: (obj, { input }) => updateUser(input),
    deleteUser: (obj, { id }) => deleteUser(id),
  }
}

export { userWebResolver };