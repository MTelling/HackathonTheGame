import React, { Component } from 'react';
import { Link } from 'react-router';
import Paper from 'material-ui/Paper';

export default class Output extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
        <Paper className="outputContainer" zDepth={2} rounded={false}>
          <p className="output">
            test
            {this.props.output}
          </p>
        </Paper>
    );
  }
}
