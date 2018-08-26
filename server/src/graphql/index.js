import { importSchema } from 'graphql-import';
import { makeExecutableSchema } from 'graphql-tools';
import { userResolver, testResolver } from '../resolvers';

const schema = makeExecutableSchema({
  typeDefs: importSchema(`${__dirname}/index.graphql`),
  resolvers: [userResolver ,testResolver],
});

export default schema;