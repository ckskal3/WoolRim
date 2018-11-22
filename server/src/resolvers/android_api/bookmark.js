import { Bookmark, User } from '../../model';
import { getUser } from './user';
import { getRecording } from '../web_api/recording';

const getBookmarkList = async (stu_id) => {
  try {
    const user = await User.where({stu_id,}).one();
    if(!user){ return [] };
    return await Bookmark.where({user_id: user.id});
  } catch (err) {
    console.log('getBookmarkList has err : ', err);
    return [];
  }
}

const createBookmark = async (input) => {
  input.created = new Date();
  try {
    await Bookmark.create(input);
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('createBookmark has err : ', err);
    return {
      isSuccess: false,
    };
  }
}

const deleteBookmarkById = async (id) => {
  try {
    await Bookmark.delete({ id, });
    return {
      isSuccess: true,
    };
  } catch (err) {
    console.log('deleteBookmark has err : ', err)
    return {
      isSuccess: false,
    };
  }
}

const deleteBookmark = async (user_id, recording_id) => {
  await Bookmark.where({user_id, recording_id}).delete();
  return {
    isSuccess: true,
  }
}

const bookmarkResolver = {
  Bookmark: {
    user: (obj) => getUser(obj.user_id),
    recording: (obj) => getRecording(obj.recording_id),
  },
  Query: {
    getBookmarkList: (obj, {stu_id}) => getBookmarkList(stu_id),
  },
  Mutation: {
    createBookmark: (obj, { input }) => createBookmark(input),
    deleteBookmarkById: (obj, { id }) => deleteBookmarkById(id),
    deleteBookmark: (obj, { user_id, recording_id }) => deleteBookmark(user_id, recording_id),
  }
}

export { bookmarkResolver };