import React, { Component } from 'react';
import { Link } from 'react-router';
import RaisedButton from 'material-ui/RaisedButton';
import Editor from './Editor';

export default class Game extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <h1>Game page</h1>
        <Editor />
      </div>
    );
  }
}
