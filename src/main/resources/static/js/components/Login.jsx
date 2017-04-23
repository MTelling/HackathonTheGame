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

  componentDidMount(){
    var socket = new SockJS('/htg');
    this.stompClient = Stomp.over(socket);
    this.stompClient.debug = null;

    var that = this;
    this.stompClient.connect({}, function (frame) {
      console.log('Connected: ' + frame);
      that.stompClient.subscribe('/user/queue/login', function (status) {
        var res = JSON.parse(status.body);
        var status = res.status;
          if (status === "success") {
            that.context.store.setUser({ username: res.username });
            that.props.router.push('/game');
          } else if (status === "exists") {
            that.setState({ exists: true });
            console.log("EXIST!!");
          }
      });
    });
  }

  handleChange(e) {
    this.setState({
      exists: false,
      value: e.target.value,
    });
  };

  handleSubmit(e) {
    e.preventDefault();
   console.log("Trying to login as " + this.state.value);
   // this.context.store.login(this.state.value);
   this.stompClient.send("/app/login", {}, JSON.stringify({"username": this.state.value}));
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
