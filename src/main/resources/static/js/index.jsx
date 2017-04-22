import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, IndexRoute, Redirect, hashHistory } from 'react-router';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import darkBaseTheme from 'material-ui/styles/baseThemes/darkBaseTheme';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import Login from './components/Login';
import Game from './components/Game';
// import '../css/general.css';

import store from './store';
import injectTapEventPlugin from 'react-tap-event-plugin';
injectTapEventPlugin();

class App extends Component {
  constructor(props) {
    super(props);
    this.store = store();
  }

  getChildContext() {
    return { store: this.store };
  }

  componentDidMount(){
    this.store.connect(this.props.router);
  }

  render() {
    return (
      // <MuiThemeProvider muiTheme={getMuiTheme(darkBaseTheme)}>
      <MuiThemeProvider>
        <div className="container">
          {this.props.children}
        </div>
      </MuiThemeProvider>
    );
  }
}

App.childContextTypes = {
    store: React.PropTypes.object,
}

ReactDOM.render(
    <Router history={hashHistory}>
      <Route path="/" component={App}>
        <IndexRoute component={Login} />
        <Route path="game" component={Game} />
      </Route>
    </Router>, document.getElementById('root'));
