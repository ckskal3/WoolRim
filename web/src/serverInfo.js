const addr = process.env.NODE_ENV === 'production'? '13.125.221.121' : 'localhost';

const serverInfo = {
  serverURL: `http://${addr}:3000/graphql`,
  fileServerURL: `http://${addr}:4000/`,
  webServerURL: `http://${addr}:5000/`,
}

export default serverInfo;
