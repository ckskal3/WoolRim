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

const createNotice = async (input_list) => {
  input_list.map(item => {
    item.created = new Date();
  });
  try {
    await Notice.createBulk(input_list);
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('createNotice has err : ', err);
    return {
      isSuccess: false,
    };
  }
}

const updateNotice = async (notice_list) => {
  try {
    notice_list.map(async notice => {
      if (notice.content) {
        await Notice.find(notice.id).update({ content: notice.content })
      }
    })
    return {
      isSuccess: true,
    }
  } catch (err) {
    console.log('updateNotice has err : ', err);
    return {
      isSuccess: false,
    };
  }
}

const deleteNotice = async (id_list) => {
  try {
    await Notice.delete({ id: id_list });
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('deleteNotice has err : ', err)
    return {
      isSuccess: false,
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
    createNotice: (obj, { input_list }) => createNotice(input_list),
    updateNotice: (obj, { input_list }) => updateNotice(input_list),
    deleteNotice: (obj, { id_list }) => deleteNotice(id_list),
  }
}

export { noticeResolver };