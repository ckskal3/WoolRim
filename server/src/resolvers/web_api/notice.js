import { Notice } from '../../model';
import { getUser } from './user';

const getAllNotice = async () => {
  return await Notice.query();
}

const createNotice = async (input) => {
  input.created = new Date();
  await Notice.create(input);
  return true;
}

const updateNotice = async (notice) => {
  await Notice.find(notice.id).update({ content: notice.content });
  return true;
}

const deleteNotice = async (id) => {
  await Notice.delete({ id });
  return true;
}

const noticeWebResolver = {
  Notice: {
    user: (obj) => getUser(obj.user_id),
  },
  Query: {
    getAllNotice: () => getAllNotice(),
  },
  Mutation: {
    createNotice: (obj, { input }) => createNotice(input),
    updateNotice: (obj, { input }) => updateNotice(input),
    deleteNotice: (obj, { id }) => deleteNotice(id),
  }
}

export { noticeWebResolver };