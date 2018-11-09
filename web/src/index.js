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

import LoginView from './LoginView';

ReactDOM.render(
  <Router>
    <div>
      <Route path="/" component={LoginView} />
    </div>
  </Router>
  , document.getElementById('root')
);
registerServiceWorker();
