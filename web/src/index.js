import React from 'react';
import ReactDOM from 'react-dom';
import registerServiceWorker from './registerServiceWorker';
import {
  BrowserRouter as Router,
  Route,
} from 'react-router-dom';

import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './common/header'
import { UserContainer, PoetContainer, PoemContainer, NoticeContainer, RecordingContainer } from './container'

ReactDOM.render(
  <Router>
    <div>
      <Header />
      <Route path="/user" component={UserContainer} />
      <Route path="/poet" component={PoetContainer} />
      <Route path="/poem" component={PoemContainer} />
      <Route path="/notice" component={NoticeContainer} />
      <Route path="/recording" component={RecordingContainer} />
    </div>
  </Router>
  , document.getElementById('root')
);
registerServiceWorker();
