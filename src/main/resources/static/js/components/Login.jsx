import React, { Component } from 'react';
import { Link } from 'react-router';
import Paper from 'material-ui/Paper';
import LoginForm from './LoginForm';

export default class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      exists: false,
      value: "",
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(e) {
    this.setState({
      exists: false,
      value: e.target.value,
    });
  };

  handleSubmit(e) {
    e.preventDefault();
    this.context.store.login(this.state.value);
    console.log("Submit username \"" + this.state.value + "\"");
  }

  render() {
    return (
      <div style={{padding: "15% 0"}}>
        <Paper className="loginContainer" zDepth={2} >
          <div id="loginErrorContainer" style={{display:!this.state.exists?"none":"inline-block"}}>
            <span style={{color:"#EF5350"}}>{this.state.value}</span> is already in use. Try another!
          </div>
          <LoginForm
            value={this.state.value}
            onChange={this.handleChange}
            onSubmit={this.handleSubmit}/>
        </Paper>
        <div className="loginFooter">HackthonTheGame</div>
      </div>
    );
  }
}

Login.contextTypes = {
  store: React.PropTypes.object,
};
