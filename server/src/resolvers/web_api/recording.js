import { Recording } from '../../model';
import { getUser } from './user';
import { getPoem } from './poem';
import { createNotification } from './notification';
import * as _ from 'lodash';

export const getRecording = async (id) => {
  return await Recording.find(id);
}

const deleteRecordingById = async (id) => {
  await Recording.find(id).delete();
  return true;
}

  return true;
}

const acceptRecording = async (recording_id) => {
  await Recording.find(recording_id).update({auth_flag: 1});
  const recording = await Recording.find(recording_id).include('user').include('poem');
  await createNotification({
    user_id: recording.user.id,
    content: `${recording.poem.name} 울림이 승인되었습니다.`
  })
  return true;
} 

const rejectRecording = async (recording_id) => {
  await Recording.find(recording_id).update({auth_flag: -1});
  const recording = await Recording.find(recording_id).include('user').include('poem');
  await createNotification({
    user_id: recording.user.id,
    content: `${recording.poem.name} 울림이 거절되었습니다.`
  })
  return true;
} 

const getAllRecordingForAudit = async () => {
  const recording_list = await Recording.where({auth_flag: 0}).include('user');
  const user_list = _.uniqBy(recording_list,'user_id');
  const result_promise = user_list.map(async item => {
    const recording_list = await Recording.where({auth_flag: 0, user_id: item.user_id});
    return {
      user: item.user,
      recording_list,
    }
  })
  return await Promise.all(result_promise);
}

const recordingWebResolver = {
  Recording: {
    user: (obj) => getUser(obj.user_id),
    poem: (obj) => getPoem(obj.poem_id),
  },
  Status: {
    REJECTED: -1,
    ACCEPTED: 1,
    WAITING: 0,
  },
  Query: {
    getRecording: (obj, { id }) => getRecording(id),
    getAllRecordingForAudit: () => getAllRecordingForAudit(),
  },
  Mutation: {
    acceptRecording: (obj, {recording_id}) => acceptRecording(recording_id),
    rejectRecording: (obj, {recording_id}) => rejectRecording(recording_id),
    deleteRecordingById: (obj, { id }) => deleteRecordingById(id),
  }
}

export { recordingWebResolver };