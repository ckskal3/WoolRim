import { Recording, User, Poem } from '../../model';
import { getUser } from '../web_api/user';
import { getPoem } from '../web_api/poem';

export const getAllRecording = async (stu_id) => {
  try {
    console.log(stu_id);
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
    if(user_id){
      return await Recording.where({ poem_id, user_id });
    }else{
      return await Recording.where({ poem_id });
    }
  } catch (err) {
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
  if (user.gender === '남자') {
    await Poem.where({ name: input.poem_name }).update({ auth_count_man: poem_result[0].auth_count_man + 1 });
  } else {
    await Poem.where({ name: input.poem_name }).update({ auth_count_woman: poem_result[0].auth_count_woman + 1 });
  }
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

const applyRecording = async (id_list) => {
  id_list.map(async (id) => {
    const recording = await Recording.find(id).include('user').include('poem');
    const sum_point = recording.user.bongsa_time + recording.poem.point;
    await User.find(recording.user.id).update({ bongsa_time: sum_point });
  })
  return true;
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
    applyRecording: (obj, { id_list }) => applyRecording(id_list),
    createRecording: (obj, { input }) => createRecording(input),
    deleteRecording: (obj, { input }) => deleteRecording(input),
  }
}

export { recordingResolver };