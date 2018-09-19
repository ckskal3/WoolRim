import React from 'react';
import ReactDOM from 'react-dom';
import registerServiceWorker from './registerServiceWorker';
import {
  BrowserRouter as Router,
  Route,
} from 'react-router-dom';

import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './common/header'
import { UserContainer, Poet, Poem, Notice, Recording } from './container'

ReactDOM.render(
  <Router>
    <div>
      <Header />
      <Route path="/user" component={UserContainer} />
      <Route path="/poet" component={Poet} />
      <Route path="/poem" component={Poem} />
      <Route path="/notice" component={Notice} />
      <Route path="/recording" component={Recording} />
    </div>
  </Router>
  , document.getElementById('root')
);
registerServiceWorker();
