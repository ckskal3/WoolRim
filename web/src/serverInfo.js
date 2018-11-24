const addr = process.env.NODE_ENV === 'production'? '13.125.221.121' : 'localhost';

const serverInfo = {
  serverURL: `http://${addr}:3000/graphql`,
}

export default serverInfo;
