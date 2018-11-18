import axios from 'axios';
import serverInfo from '../../serverInfo';

const getAllPoet = async () => {
  const query = `query {
    getAllPoet{
      id
      name
    }
  }`;
  const result = await axios.post(serverInfo.serverURL, { query });
  return result.data.data.getAllPoet;
}

const updatePoet = async (input) => {
  const query = `mutation($input:UpdatePoetInput!){
    updatePoet(input:$input)
  }`
  const result = await axios.post(serverInfo.serverURL,
    {
      query,
      variables: {
        input,
      }
    }
  );
  return result.data.data.updatePoet;
}

const deletePoet = async (id) => {
  console.log(id);
  const query = `mutation($id:ID!){
    deletePoet(id:$id)
  }`
  const result = await axios.post(serverInfo.serverURL,
    {
      query,
      variables: {
        id
      }
    }
  );
  return result.data.data.deletePoet;
}

const createPoet = async (input) => {
  const query = `mutation($input:CreatePoetInput!){
    createPoet(input:$input)
  }`
  const result = await axios.post(serverInfo.serverURL,
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