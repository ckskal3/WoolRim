import axios from 'axios';
import serverInfo from '../../serverInfo';

const getAllNotice = async () => {
  const query = `query {
    getAllNotice{
      id
      content
      created
      user{
        name
      }
    }
  }`;
  const result = await axios.post(serverInfo.serverURL, { query });
  return result.data.data.getAllNotice;
}

const updateNotice = async (input) => {
  
  const query = `mutation($input:UpdateNoticeInput!){
    updateNotice(input:$input)
  }`
  const result = await axios.post(serverInfo.serverURL,
    {
      query,
      variables: {
        input,
      }
    }
  );
  return result.data.data.updateNotice;
}

const deleteNotice = async (id) => {
  const query = `mutation($id:ID!){
    deleteNotice(id:$id)
  }`
  const result = await axios.post(serverInfo.serverURL,
    {
      query,
      variables: {
        id
      }
    }
  );
  return result.data.data.deleteNotice;
}

const createNotice = async (input) => {
  const query = `mutation($input:CreateNoticeInput!){
    createNotice(input:$input)
  }`
  const result = await axios.post(serverInfo.serverURL,
    {
      query,
      variables: {
        input,
      }
    }
  );
  return result.data.data.createNotice;
}


export { getAllNotice, deleteNotice, createNotice, updateNotice }