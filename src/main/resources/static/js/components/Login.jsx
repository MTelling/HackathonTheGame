import React, { Component } from 'react';
import { Link } from 'react-router';
import Paper from 'material-ui/Paper';
import LoginForm from './LoginForm';

export default class Login extends Component {
  constructor(props) {
    super(props);
    this.state = { value: "" };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(e) {
    this.setState({
      value: e.target.value,
    });
  };

  handleSubmit(e) {
    e.preventDefault();
    alert("Submit username \"" + this.state.value + "\"");
  }

  render() {
    return (
        <Paper className="loginContainer" zDepth={2} >
          <div id="loginErrorContainer" style={{display:"none"}}>
            <p>User name is already in use. Try another!</p>
          </div>
          <LoginForm
            value={this.state.value}
            onChange={this.handleChange}
            onSubmit={this.handleSubmit}/>
        </Paper>
    );
  }
}
