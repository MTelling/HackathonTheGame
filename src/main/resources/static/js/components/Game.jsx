import React, { Component } from 'react';
import { Link } from 'react-router';
import Drawer from 'material-ui/Drawer';
import RaisedButton from 'material-ui/RaisedButton';
import Editor from './Editor';
import Output from './Output';

export default class Game extends Component {
  constructor(props) {
    super(props);
    this.state = { open: false };
    this.handleToggle = this.handleToggle.bind(this);
  }

  handleToggle() {
    this.setState({ open: !this.state.open });
  }

  render() {
    return (
      <div className="gameContainer">
        <h1>Challenge</h1>
        <h3>Welcome {this.context.store.user.username}</h3>
        <RaisedButton
          label="Toggle Drawer"
          onTouchTap={this.handleToggle}/>
        <Drawer open={this.state.open}>
          challenge 123
        </Drawer>
        <Editor />
        <Output />
      </div>
    );
  }
}

Game.contextTypes = {
  store: React.PropTypes.object,
};
