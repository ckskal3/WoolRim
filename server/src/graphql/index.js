import { importSchema } from 'graphql-import';
import { makeExecutableSchema } from 'graphql-tools';
import {
  userResolver,
  poetResolver,
  poemResolver,
  noticeResolver,
  recordingResolver,
  notificationResolver,
  bookmarkResolver,
} from '../resolvers';

const schema = makeExecutableSchema({
  typeDefs: importSchema(`${__dirname}/index.graphql`),
  resolvers: [
    userResolver,
    poetResolver,
    poemResolver,
    noticeResolver,
    recordingResolver,
    notificationResolver,
    bookmarkResolver,
  ],
});

export default schema;