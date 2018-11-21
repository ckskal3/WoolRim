import { importSchema } from 'graphql-import';
import { makeExecutableSchema } from 'graphql-tools';
import {
  userWebResolver,
  poetWebResolver,
  poemWebResolver,
  noticeWebResolver,
  recordingWebResolver,
  notificationWebResolver,
  bookmarkWebResolver,
} from '../resolvers/web_api';
import {
  userResolver,
  poetResolver,
  poemResolver,
  noticeResolver,
  recordingResolver,
  notificationResolver,
  bookmarkResolver,
} from '../resolvers/android_api';

const schema = makeExecutableSchema({
  typeDefs: importSchema(`${__dirname}/index.graphql`),
  resolvers: [
    userWebResolver,
    poetWebResolver,
    poemWebResolver,
    noticeWebResolver,
    recordingWebResolver,
    notificationWebResolver,
    bookmarkWebResolver,
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