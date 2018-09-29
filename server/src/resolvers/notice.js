import { Notice } from '../model';
import { getUser } from './user';

const getAllNotice = async () => {
  try {
    return await Notice.query();
  } catch (err) {
    console.log('getAllNotice has err : ', err);
    return null;
  }
}

const getNotice = async (id) => {
  try {
    return await Notice.find(id);
  } catch (err) {
    console.log('getNotice has err : ', err);
    return null;
  }
}

const createNotice = async (input_notice) => {
  const notice = new Notice({
    content: input_notice.content,
    user_id: input_notice.user_id,
  });
  try {
    await notice.save();
    return {
      item: notice,
      isSuccess: true,
      msg: '공지사항 생성 완료',
    };
  } catch (err) {
    console.log('createNotice has err : ', err);
    return {
      isSuccess: false,
      msg: err,
    };
  }
}

const updateNotice = async (id, notice) => {
  try {
    if (notice.content) {
      await Notice.find(id).update({ content: notice.content });
    }
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('updateNotice has err : ', err);
    return {
      isSuccess: false,
      msg: err,
    };
  }
}

const deleteNotice = async (id) => {
  try {
    await Notice.delete({ id });
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('deleteNotice has err : ', err)
    return {
      isSuccess: false,
      msg: err,
    };
  }
}

const noticeResolver = {
  Notice: {
    user: (obj) => getUser(obj.user_id),
  },
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