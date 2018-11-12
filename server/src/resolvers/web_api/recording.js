import { Recording } from '../../model';
import { getUser } from './user';
import { getPoem } from './poem';

const getAllRecording = async () => {
  return await Recording.query();
}

const getRecording = async (id) => {
  return await Recording.find(id);
}

const deleteRecording = async (id) => {
  await Recording.delete(id);
}

const deleteAllRecording = async () => {
  await Recording.delete();
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
    getAllRecording: () => getAllRecording(),
    getRecording: (obj, { id }) => getRecording(id),
  },
  Mutation: {
    deleteAllRecording: () => deleteAllRecording(),
    deleteRecording: (obj, { input }) => deleteRecording(input),
  }
}

export { recordingWebResolver };