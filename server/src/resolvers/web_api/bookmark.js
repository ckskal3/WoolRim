import { Bookmark, User } from '../model';
import { getUser } from './user';
import { getRecording } from './recording';

const getAllBookmark = async () => {
  return await Bookmark.query();
}

const createBookmark = async (input) => {
  input.created = new Date();
  await Bookmark.create(input);
  return true;
}

const deleteBookmark = async (id) => {
  await Bookmark.delete({ id });
  return true;
}

const bookmarkWebResolver = {
  Bookmark: {
    user: (obj) => getUser(obj.user_id),
    recording: (obj) => getRecording(obj.recording_id),
  },
  Query: {
    getAllBookmark: () => getAllBookmark(),
  },
  Mutation: {
    createBookmark: (obj, { input }) => createBookmark(input),
    deleteBookmark: (obj, { id }) => deleteBookmark(id),
  }
}

export { bookmarkWebResolver };