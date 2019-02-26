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
import { UserContainer, PoetContainer, PoemContainer, NoticeContainer, RecordingContainer, AuditContainer } from './container'
import Header from './common/Header'
import LoginView from './LoginView';

const userInfo = {
  name: '관리자',
  id: 6,
}

ReactDOM.render(
  <Router>
    <LoginView/>
  </Router>
  , document.getElementById('root')
);
registerServiceWorker();
