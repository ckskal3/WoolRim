import React from 'react';
import ReactDOM from 'react-dom';
import registerServiceWorker from './registerServiceWorker';
import {
  BrowserRouter as Router,
  Route,
} from 'react-router-dom';

import 'bootstrap/dist/css/bootstrap.min.css';
import '@blueprintjs/core/lib/css/blueprint.css';
import '@blueprintjs/table/lib/css/table.css';
import { UserContainer, PoetContainer, PoemContainer, NoticeContainer, RecordingContainer } from './container'
import Header from './common/Header'

const userInfo = {
  name: '관리자',
  id: 6,
}

ReactDOM.render(
  <Router>
    <div>
      <Header user={userInfo} />
      <Route path="/user" component={UserContainer} />
      <Route path="/poet" component={PoetContainer} />
      <Route path="/poem" component={PoemContainer} />
      <Route path="/notice" render={props => <NoticeContainer {...props} current_account={userInfo} />} />
      <Route path="/recording" component={RecordingContainer} />
    </div>
  </Router>
  , document.getElementById('root')
);
registerServiceWorker();
