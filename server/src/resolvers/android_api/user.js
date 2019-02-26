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

const adminLogin = async (stu_id, passwd) => {
  const user = await User.where({ stu_id, }).one();
  if (!user.admin) {
    return false;
  }
  const encryption = await pbkdf2.pbkdf2Sync(passwd, user.name, 30, 32, 'sha512');
  if (user.passwd === encryption.toString()) {
    return true;
  } else {
    return false;
  }
}

const getMainInfo = async (stu_id) => {
  const user = await User.where({ stu_id, }).one();
  if (user) {
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

const modifyUser = async (input) => {
  if (input.passwd) {
    const encryption = await pbkdf2.pbkdf2Sync(input.passwd, input.name, 30, 32, 'sha512');
    await User.find(input.id).update({
      name: input.name,
      passwd: encryption,
      profile: input.profile,
    })
    return {
      isSuccess: true,
    }
  }
  await User.find(input.id).update({
    name: input.name,
    profile: input.profile,
  })
  return {
    isSuccess: true,
  }
}
const userResolver = {
  LoginResult: {
    unreadCount: (obj) => getUnreadCount(obj.user.id),
    recording_list: (obj) => getAllRecordingByLogin(obj.user.stu_id),
    notification_list: (obj) => getNotificationByLogin(obj.user.stu_id),
  },
  Query: {
    getMainInfo: (obj, { stu_id }) => getMainInfo(stu_id),
  },
  Mutation: {
    modifyUser: (obj, { input }) => modifyUser(input),
    createUser: (obj, { input }) => createUser(input),
    login: (obj, { stu_id, passwd }) => login(stu_id, passwd),
    adminLogin: (obj, { stu_id, passwd }) => adminLogin(stu_id, passwd),
  }
}

export { userResolver };