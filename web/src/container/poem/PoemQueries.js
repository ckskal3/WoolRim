import axios from 'axios';
import serverInfo from '../../serverInfo';

const getAllPoem = async () => {
  const query = `query {
    getAllPoemForWeb{
      id
      name
      poet{
        name
      }
      content
      point
      length
      auth_count
      auth_count_woman
      auth_count_man
    }
  }`;
  const result = await axios.post(serverInfo.serverURL, { query });
  return result.data.data.getAllPoemForWeb;
}

const updatePoem = async (input) => {
  const query = `mutation($input:UpdatePoemInput!){
    updatePoem(input:$input)
  }`
  const result = await axios.post(serverInfo.serverURL,
    {
      query,
      variables: {
        input,
      }
    }
  );
  return result.data.data.updatePoem;
}

const deletePoem = async (id) => {
  const query = `mutation($id:ID!){
    deletePoem(id:$id)
  }`
  const result = await axios.post(serverInfo.serverURL,
    {
      query,
      variables: {
        id
      }
    }
  );
  return result.data.data.deletePoem;
}

const createPoem = async (input) => {
  const query = `mutation($input:CreatePoemInput!){
    createPoem(input:$input)
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