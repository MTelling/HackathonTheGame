import React, { Component } from 'react';
import { Link } from 'react-router';
import FlatButton from 'material-ui/FlatButton';
import Dialog from 'material-ui/Dialog';
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from 'material-ui/Table';
import ContentInbox from 'material-ui/svg-icons/content/inbox';

export default class UserInfo extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    const actions = [
      <FlatButton
        label="Close"
        primary={true}
        onTouchTap={this.props.onClose}
      />,
    ];

    return (
      <Dialog
        title={this.props.username}
        actions={actions}
        modal={true}
        open={this.props.open}>
        <div>Score: {this.props.score}</div>
      </Dialog>
    );
  }
}
