import { Poem, Recording } from '../model';
import * as _ from 'lodash'

const func = async () => {
  const recording_list = await Recording.where({auth_flag: 0}).include('user');
  const user_list = _.uniqBy(recording_list,'user_id');
  const result = user_list.map(async (item) => {
    const recording_list = await Recording.where({auth_flag: 0, user_id: item.user_id});
    console.log(item.user);
    console.log(recording_list);
    return {
      user: item.user,
      recording_list,
    }
  })
  console.log(result)
}

func();