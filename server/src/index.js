import express from 'express';
import graphqlHTTP from 'express-graphql';
import schema from './graphql/index'
var app = express();

app.use('/graphql', graphqlHTTP({
  schema,
  graphiql: true,
}));

app.listen(3000);