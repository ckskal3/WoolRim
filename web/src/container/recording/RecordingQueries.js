import axios from 'axios';
import serverInfo from '../../serverInfo';

const getAllRecording = async () => {
  const query = `query {
    getAllRecording{
      recording_list{
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
    }
  }`;
  const result = await axios.post(serverInfo.serverURL, { query });
  return result.data.data.getAllRecording.recording_list;
}

const deleteRecording = async (id) => {
  const query = `mutation($id:ID!){
    deleteRecordingById(id:$id)
  }`
  const result = await axios.post(serverInfo.serverURL,
    {
      query,
      variables: {
        id
      }
    }
  );
  return result.data.data.deleteRecording;
}

export { getAllRecording, deleteRecording }
