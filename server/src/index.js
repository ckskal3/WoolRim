import express from 'express';
import graphqlHTTP from 'express-graphql';
import schema from './graphql/index';

const app = express();

app.use('/graphql', graphqlHTTP({
  schema,
  graphiql: true,
}));

app.listen(3000, 'localhost', () => {
  console.log('localhost:3000 연결 !!');
});