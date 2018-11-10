import { Bookmark, User } from '../model';
import { getUser } from './user';
import { getRecording } from './recording';

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

const deleteBookmark = async (id_list) => {
  try {
    await Bookmark.delete({ id: id_list });
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
    deleteBookmark: (obj, { id_list }) => deleteBookmark(id_list),
  }
}

export { bookmarkResolver };