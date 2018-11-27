import { Notification } from '../../model';

const getAllNotification = async () => {
  return await Notification.query();
}

const createNotification = async (input) => {
  input.created = new Date();
  await Notification.create(input);
  return true;
}

const deleteNotification = async (id) => {
  await Notification.delete(id);
  return true;
}

const deleteAllNotification = async () => {
  await Notification.delete();
}

const notificationWebResolver = {
  Query: {
    getAllNotification: () => getAllNotification(),
  },
  Mutation: {
    deleteAllNotification: () => deleteAllNotification(),
    createNotification: (obj, { input }) => createNotification(input),
    deleteNotification: (obj, { id }) => deleteNotification(id),
  }
}

export { notificationWebResolver, createNotification };