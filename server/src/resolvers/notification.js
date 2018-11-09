import { Notification, User } from '../model';

export const getNotificationByLogin = async (stu_id) => {
  try {
    const user = await User.where({ stu_id }).one();
    if (!user) {
      return [];
    }
    const result = await Notification.where({ user_id: user.id });
    if (result.length === 0) {
      return [];
    }
    return result;
  } catch (err) {
    console.log('getNotification has err : ', err);
    return [];
  }
}

const getNotification = async (stu_id) => {
  try {
    const user = await User.where({ stu_id }).one();
    if (!user) {
      return { isSuccess: false, notification_list: [] };
    }
    const result = await Notification.where({ user_id: user.id });
    if (result.length === 0) {
      return { isSuccess: false, notification_list: [] };
    }
    return {
      isSuccess: true,
      notification_list: result,
    };
  } catch (err) {
    console.log('getNotification has err : ', err);
    return null;
  }
}

const getUnreadCount = async (user_id) => {
  try{
    return await Notification.where({user_id, read_flag: false}).count();
  }catch(err){
    return -1;
  }
}

const createNotification = async (input_list) => {
  input_list.map(item => {
    item.created = new Date();
  });
  try {
    await Notification.createBulk(input_list);
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('createNotification has err : ', err);
    return {
      isSuccess: false,
    };
  }
}

const deleteNotification = async (id_list) => {
  try {
    await Notification.delete(id_list);
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('createNotification has err : ', err);
    return {
      isSuccess: false,
    };
  }
}

const notificationResolver = {
  Query: {
    getNotification: (obj, { stu_id }) => getNotification(stu_id),
    getUnreadCount: (obj, { user_id }) => getUnreadCount(user_id),
  },
  Mutation: {
    createNotification: (obj, { input_list }) => createNotification(input_list),
    deleteNotification: (obj, { id_list }) => deleteNotification(id_list),
  }
}

export { notificationResolver };