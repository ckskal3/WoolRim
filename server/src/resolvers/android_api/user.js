import { User } from '../../model';
import { getNotificationByLogin } from './notification'
import { getAllRecordingByLogin, getAllRecording } from './recording'
import { getUnreadCount } from '../android_api'
import pbkdf2 from 'pbkdf2'

const createUser = async (input_user) => {
  const encryption = await pbkdf2.pbkdf2Sync(input_user.passwd, input_user.name, 30, 32, 'sha512');
  const user = new User({
    name: input_user.name,
    univ: input_user.univ,
    stu_id: input_user.stu_id,
    gender: input_user.gender,
    passwd: encryption.toString(),
    created: new Date(),
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
    };
  }
}

const login = async (stu_id, passwd) => {
  const user = await User.where({ stu_id, }).one();
  const encryption = await pbkdf2.pbkdf2Sync(passwd, user.name, 30, 32, 'sha512');
  if (user.passwd === encryption.toString()) {
    return {
      isSuccess: true,
      user,
    }
  } else {
    return {
      isSuccess: false,
    }
  }
}

const userResolver = {
  LoginResult: {
    unreadCount: (obj) => getUnreadCount(obj.user.id),
    recording_list: (obj) => getAllRecordingByLogin(obj.user.stu_id),
    notification_list: (obj) => getNotificationByLogin(obj.user.stu_id),
  },
  Mutation: {
    createUser: (obj, { input }) => createUser(input),
    login: (obj, { stu_id, passwd }) => login(stu_id, passwd),
  }
}

export { userResolver };