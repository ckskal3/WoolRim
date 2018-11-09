import axios from 'axios';
import serverInfo from '../../serverInfo';

const getAllUser = async () => {
  const query = `query {
    getAllUser{
      id
      name
      univ
      stu_id
      gender
      created
      bongsa_time
      profile
    }
  }`;
  const result = await axios.post(serverInfo.serverURL, { query });
  return result.data.data.getAllUser;
}

export { getAllUser }