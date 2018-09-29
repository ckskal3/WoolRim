import { Recording } from '../model';
import { getUser } from './user';
import { getPoem } from './poem';

const getAllRecording = async () => {
  try {
    return await Recording.query();
  } catch (err) {
    console.log('getAllRecording has err : ', err);
    return null;
  }
}

const getRecording = async (id) => {
  try {
    return await Recording.find(id);
  } catch (err) {
    console.log('getRecording has err : ', err);
    return null;
  }
}

const createRecording = async (input_recording) => {
  const recording = new Recording({
    path: input_recording.path,
    user_id: input_recording.user_id,
    duration: input_recording.duration,
    poem_id: input_recording.poem_id,
  });
  try {
    await recording.save();
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('createRecording has err : ', err);
    return {
      isSuccess: false,
      msg: err,
    };
  }
}

const deleteRecording = async (id) => {
  try {
    await Recording.delete({ id });
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('deleteRecording has err : ', err);
    return {
      isSuccess: false,
      msg: err,
    };
  }
}

const recordingResolver = {
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
    createRecording: (obj, { input }) => createRecording(input),
    deleteRecording: (obj, { id }) => deleteRecording(id),
  }
}

export { recordingResolver };