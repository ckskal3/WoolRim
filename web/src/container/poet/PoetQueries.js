import axios from 'axios';
import serverInfo from '../../serverInfo';

const getAllPoet = async () => {
  const query = `query {
    getAllPoet{
      id
      name
    }
  }`;
  const result = await axios.post(serverInfo.urlDev, { query });
  return result.data.data.getAllPoet;
}

const updatePoet = async (input_list) => {
  const query = `mutation($input_list:[UpdatePoetInput]){
    updatePoet(input_list:$input_list){
      isSuccess
    }
  }`
  const result = await axios.post(serverInfo.urlDev,
    {
      query,
      variables: {
        input_list,
      }
    }
  );
  return result.data.data.updatePoet;
}

const deletePoet = async (id_list) => {
  const query = `mutation($id_list:[ID]!){
    deletePoet(id_list:$id_list){
      isSuccess
    }
  }`
  const result = await axios.post(serverInfo.urlDev,
    {
      query,
      variables: {
        id_list
      }
    }
  );
  return result.data.data.deletePoet;
}

const createPoet = async (input) => {
  const query = `mutation($input:[CreatePoetInput]!){
    createPoet(input_list:$input){
      isSuccess
    }
  }`
  const result = await axios.post(serverInfo.urlDev,
    {
      query,
      variables: {
        input,
      }
    }
  );
  return result.data.data.createPoet;
}


export { getAllPoet, deletePoet, createPoet, updatePoet }