import { Recording, User, Poem } from '../model';
import { getUser } from './user';
import { getPoem } from './poem';

export const getAllRecording = async (stu_id) => {
  try {
    if (!stu_id) {
      const result = await Recording.query();
      return {
        isSuccess: true,
        recording_list: result,
      }
    }
    const user = await User.where({ stu_id, }).one();
    if (!user) {
      return {
        isSuccess: false,
        recording_list: [],
      }
    }
    const result = await Recording.where({ user_id: user.id });
    if (result.length === 0) {
      return {
        isSuccess: false,
        recording_list: [],
      }
    }
    return {
      isSuccess: true,
      recording_list: result,
    }
  } catch (err) {
    console.log('getAllRecording has err : ', err);
    return {
      isSuccess: false,
      recording_list: [],
    };
  }
}

export const getAllRecordingByLogin = async (stu_id) => {
  try {
    const user = await User.where({ stu_id, }).one();
    if (!user) {
      return [];
    }
    const result = await Recording.where({ user_id: user.id });
    if (result.length === 0) {
      return [];
    }
    return result;
  } catch (err) {
    console.log('getAllRecording has err : ', err);
    return [];
  }
}

export const getRecordingForPlay = async (poem_id, user_id) => {
  try {
    const result = await Recording.where({ poem_id, user_id });
    return result;
  } catch (err){
    return [];
  }
}
const createRecording = async (input) => {
  const poem = await Poem.where({ name: input.poem_name }).include('poet');
  const poem_result = poem.filter(v => {
    if (v.poet.name === input.poet_name) {
      return true;
    }
    return false;
  })
  if (poem_result.length === 0) {
    return {
      isSuccess: false,
    }
  }
  const user = await User.where({ stu_id: input.stu_id }).one()
  const recording = new Recording({
    path: input.path,
    user_id: user.id,
    duration: input.duration,
    poem_id: poem_result[0].id,
    created: new Date(),
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
    };
  }
}

const deleteRecording = async (input) => {
  const user_id = (await User.where({ stu_id: input.stu_id }).one()).id
  const poem = await Poem.where({ name: input.poem_name }).include('poet');
  const poem_result = poem.filter(v => {
    if (v.poet.name === input.poet_name) {
      return true;
    }
    return false;
  })
  if (poem_result.length === 0) {
    return {
      isSuccess: false,
    }
  }
  const recording = await Recording
    .where({ $and: [{ user_id }, { poem_id: poem_result[0].id, }] }).one();
  try {
    await Recording.delete({ id: recording.id });
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('deleteRecording has err : ', err);
    return {
      isSuccess: false,
    };
  }
}

const deleteRecordingById = async (id_list) => {
  try {
    await Recording.delete({ id: id_list });
    return {
      isSuccess: true,
    }
  } catch (e) {
    return {
      isSuccess: false,
    }
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
    getAllRecording: (obj, { stu_id }) => getAllRecording(stu_id),
    getRecordingForPlay: (obj, { poem_id, user_id }) => getRecordingForPlay(poem_id, user_id),
  },
  Mutation: {
    createRecording: (obj, { input }) => createRecording(input),
    deleteRecording: (obj, { input }) => deleteRecording(input),
    deleteRecordingById: (obj, { id_list }) => deleteRecordingById(id_list),
  }
}

export { recordingResolver };