import { Notice } from '../../model';
import { getUser } from '../web_api/user';

const getNotice = async (id) => {
  try {
    return await Notice.find(id);
  } catch (err) {
    console.log('getNotice has err : ', err);
    return null;
  }
}

const noticeResolver = {
  Notice: {
    user: (obj) => getUser(obj.user_id),
  },
  Query: {
    getNotice: (obj, { id }) => getNotice(id),
  },
}

export { noticeResolver };