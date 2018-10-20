import { Recording, User, Poem } from '../model';
import { getUser } from './user';
import { getPoem } from './poem';

const getAllRecording = async (stu_id) => {
  try {
    if (!stu_id) {
      return await Recording.query();
    }
    const user_id = User.where({ stu_id }).one();
    console.log(user_id);
    return Recording.where({ user_id, });
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
    .where({$and: [{ user_id }, { poem_id: poem_result[0].id, }]}).one();
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
    getAllRecording: (stu_id) => getAllRecording(stu_id),
    getRecording: (obj, { id }) => getRecording(id),
  },
  Mutation: {
    createRecording: (obj, { input }) => createRecording(input),
    deleteRecording: (obj, { input }) => deleteRecording(input),
  }
}

export { recordingResolver };