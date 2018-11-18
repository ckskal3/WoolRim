import { Recording } from '../../model';
import { getUser } from './user';
import { getPoem } from './poem';

const getRecording = async (id) => {
  return await Recording.find(id);
}

const deleteRecordingById = async (id) => {
  await Recording.delete(id);
  return true;
}

const deleteAllRecording = async () => {
  await Recording.delete();
  return true;
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
  },
  Mutation: {
    deleteAllRecording: () => deleteAllRecording(),
    deleteRecordingById: (obj, { id }) => deleteRecordingById(id),
  }
}

export { recordingWebResolver };