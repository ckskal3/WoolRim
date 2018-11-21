import { Bookmark } from '../../model';
import { getUser } from './user';
import { getRecording } from './recording';

const getAllBookmark = async () => {
  return await Bookmark.query();
}

const bookmarkWebResolver = {
  Bookmark: {
    user: (obj) => getUser(obj.user_id),
    recording: (obj) => getRecording(obj.recording_id),
  },
  Query: {
    getAllBookmark: () => getAllBookmark(),
  },
}

export { bookmarkWebResolver };