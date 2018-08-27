import { importSchema } from 'graphql-import';
import { makeExecutableSchema } from 'graphql-tools';
import {
  userResolver,
  poetResolver,
  poemResolver,
  noticeResolver,
  recordingResolver,
  testResolver
} from '../resolvers';

const schema = makeExecutableSchema({
  typeDefs: importSchema(`${__dirname}/index.graphql`),
  resolvers: [
    userResolver,
    poetResolver,
    poemResolver,
    noticeResolver,
    recordingResolver,
    testResolver],
});

export default schema;