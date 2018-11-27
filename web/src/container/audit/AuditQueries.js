import axios from 'axios';
import serverInfo from '../../serverInfo';

export const getAllRecordingForAudit = async () => {
  const query = `query {
    getAllRecordingForAudit{
      user {
        id
        stu_id
        name
      }
      recording_list{
        id
        path
        poem{
          name
          poet{
            name
          }
        }
      }
    }
  }`
  const result = await axios.post(serverInfo.serverURL, { query });
  return result.data.data.getAllRecordingForAudit;
}

export const acceptRecording = async (recording_id) => {
  const query = `mutation($recording_id: ID!) {
    acceptRecording(recording_id: $recording_id)
  }`
  const result = await axios.post(serverInfo.serverURL, { 
    query,
    variables: {
      recording_id,
    }
   });
  return result.data.data.acceptRecording;
}

export const rejectRecording = async (recording_id) => {
  const query = `mutation($recording_id: ID!) {
   rejectRecording(recording_id: $recording_id)
  }`
  const result = await axios.post(serverInfo.serverURL, { 
    query,
    variables: {
      recording_id,
    }
   });
  return result.data.data.rejectRecording;
}