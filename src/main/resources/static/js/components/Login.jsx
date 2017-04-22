import React, { Component } from 'react';
import { Link } from 'react-router';
import { Form, Text } from 'react-form'
import Paper from 'material-ui/Paper';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

export default class Login extends Component {
  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit() {
    console.log("Submit!!");
  }

  render() {
    return (
        <Paper className="loginContainer" zDepth={2} >
          <div id="loginErrorContainer" style={{display:"none"}}>
            <p>User name is already in use. Try another!</p>
          </div>
          <Form onSubmit={this.handleSubmit}>
            <TextField
              hintText="besthacker123"
              floatingLabelText="Username" />
            <RaisedButton className="loginButton" label="Login" type="submit"/>
          </Form>
        </Paper>
    );
  }
}
