import { Notification, User } from '../../model';
import * as _ from 'lodash';

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
    return _.orderBy(result, ['created'], ['desc']);
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
      notification_list: _.orderBy(result, ['created'], ['desc']),
    };
  } catch (err) {
    console.log('getNotification has err : ', err);
    return null;
  }
}

const getUnreadCount = async (user_id) => {
  try {
    return await Notification.where({ user_id, read_flag: false }).count();
  } catch (err) {
    return -1;
  }
}
const readAllNotification = async (user_id) => {
  try {
    await Notification.where({user_id}).update({read_flag:true});
    return true;
  } catch (err) {
    return false;
  }
}

const notificationResolver = {
  Query: {
    getNotification: (obj, { stu_id }) => getNotification(stu_id),
    getUnreadCount: (obj, { user_id }) => getUnreadCount(user_id),
  },
  Mutation: {
    readAllNotification: (obj, { user_id }) => readAllNotification(user_id),
  }
}

export { notificationResolver, getUnreadCount };