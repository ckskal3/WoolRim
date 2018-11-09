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

const updateNotice = async (input_list) => {
  
  const query = `mutation($input_list:[UpdateNoticeInput]){
    updateNotice(input_list:$input_list){
      isSuccess
    }
  }`
  const result = await axios.post(serverInfo.serverURL,
    {
      query,
      variables: {
        input_list,
      }
    }
  );
  return result.data.data.updateNotice;
}

const deleteNotice = async (id_list) => {
  const query = `mutation($id_list:[ID]!){
    deleteNotice(id_list:$id_list){
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
  return result.data.data.deleteNotice;
}

const createNotice = async (input_list) => {
  const query = `mutation($input_list:[CreateNoticeInput]!){
    createNotice(input_list:$input_list){
      isSuccess
    }
  }`
  const result = await axios.post(serverInfo.serverURL,
    {
      query,
      variables: {
        input_list,
      }
    }
  );
  return result.data.data.createNotice;
}


export { getAllNotice, deleteNotice, createNotice, updateNotice }