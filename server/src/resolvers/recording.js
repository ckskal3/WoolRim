import { Recording } from '../model';

const getAllRecording = async () => {
  try {
    return await Recording.query();
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const getRecording = async (id) => {
  try {
    return await Recording.find(id);
  } catch (err) {
    console.log(err);
    return 'error';
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
    return 'success';
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const deleteRecording = async (id) => {
  try {
    await Recording.delete({ id });
    return 'success';
  } catch (err) {
    console.log(err);
    return 'error';
  }
}

const recordingResolver = {
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