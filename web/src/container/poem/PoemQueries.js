import axios from 'axios';
import serverInfo from '../../serverInfo';

const getAllPoem = async () => {
  const query = `query {
    getAllPoem{
      id
      name
      poet{
        name
      }
      content
      point
      length
      auth_count
    }
  }`;
  const result = await axios.post(serverInfo.serverURL, { query });
  return result.data.data.getAllPoem;
}

const updatePoem = async (input_list) => {
  const query = `mutation($input_list:[UpdatePoemInput]){
    updatePoem(input_list:$input_list){
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
  return result.data.data.updatePoem;
}

const deletePoem = async (id_list) => {
  const query = `mutation($id_list:[ID]!){
    deletePoem(id_list:$id_list){
      isSuccess
      msg
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
  return result.data.data.deletePoem;
}

const createPoem = async (input) => {
  const query = `mutation($input:[CreatePoemInput]!){
    createPoem(input_list:$input){
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
  return result.data.data.createPoem;
}


export { getAllPoem, deletePoem, createPoem, updatePoem }