import axios from 'axios';
import serverInfo from '../../serverInfo';

const getAllRecording = async () => {
  const query = `query {
    getAllRecording{
      id
      path
      created
      auth_flag
      user{
        name
      }
      poem{
        name
        poet {
          name
        }
      }
      duration
    }
  }`;
  const result = await axios.post(serverInfo.serverURL, { query });
  return result.data.data.getAllRecording;
}

const deleteRecording = async (id_list) => {
  const query = `mutation($id_list:[ID]!){
    deleteRecordingById(id_list:$id_list){
      isSuccess
    }
  }`
  const result = await axios.post(serverInfo.serverURL,
    {
      query,
      variables: {
        id_list
      }
    }
  );
  return result.data.data.deleteRecording;
}

const createRecording = async (input) => {
  const query = `mutation($input: CreateRecordingInput!){
    createRecording(input: $input){
      isSuccess
    }
  }`
  const result = await axios.post(serverInfo.serverURL,
    {
      query,
      variables: {
        input,
      }
    }
  );
  return result.data.data.createRecording;
}


export { getAllRecording, deleteRecording, createRecording }