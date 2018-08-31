import { Notice } from '../model';

const getAllNotice = async () => {
  try {
    return await Notice.query();
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const getNotice = async (id) => {
  try {
    return await Notice.find(id);
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const createNotice = async (input_notice) => {
  const notice = new Notice({
    content: input_notice.content,
    user_id: input_notice.user_id,
  });
  try {
    await notice.save();
    return 'success';
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const updateNotice = async (id, notice) => {
  try {
    if (notice.content) {
      await Notice.find(id).update({ content: notice.content });
    }
    return 'success';
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const deleteNotice = async (id) => {
  try {
    await Notice.delete({ id });
    return 'success';
  } catch (err) {
    console.log(err)
    return 'error';
  }
}

const noticeResolver = {
  Query: {
    getAllNotice: () => getAllNotice(),
    getNotice: (obj, { id }) => getNotice(id),
  },
  Mutation: {
    createNotice: (obj, { input }) => createNotice(input),
    updateNotice: (obj, { id, input }) => updateNotice(id, input),
    deleteNotice: (obj, { id }) => deleteNotice(id),
  }
}

export { noticeResolver };