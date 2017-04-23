import React, { Component } from 'react';
import { Link } from 'react-router';
import Drawer from 'material-ui/Drawer';
import RaisedButton from 'material-ui/RaisedButton';
import Editor from './Editor';
import Output from './Output';

export default class Game extends Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      challenge: {
        name: "",
        description: "",
        initialCode: "",
      },
      code: "",
      output: "",
      username: "",
    };
    this.handleToggle = this.handleToggle.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmitCode = this.handleSubmitCode.bind(this);
  }

  componentDidMount(){
    var socket = new SockJS('/htg');
    this.stompClient = Stomp.over(socket);
    var that = this;
    this.stompClient.connect({}, function (frame) {
      console.log('Connected: ' + frame);

      that.stompClient.subscribe('/user/queue/game', function (gameResponse) {
          console.log("Got new game description!");
          var challengeDescription = JSON.parse(gameResponse.body).challengeDescription;

          if (that.state.challenge === null || challengeDescription.name !== that.state.challenge.name) {
              that.setState({
                challenge: challengeDescription,
                code: challengeDescription.initialCode +
                "\n//type your code here...\
                \n//return new Object[] {Integer.parseInt(args[0])+Integer.parseInt(args[1])};",
               });
              console.log(that.state.challenge);
          }
      });

      that.stompClient.subscribe('/user/queue/checkLogin', function (response) {
          var message = JSON.parse(response.body);

          if (message.status === "alreadyIn") {
              that.setState({
                username: message.username
              });
          }
      });

      that.stompClient.subscribe('/user/queue/pm', function (pmResponse) {
        // log to output
          console.log("Got pm response!");
          that.setState({
            output: JSON.parse(pmResponse.body).message,
          })
          console.log(that.state.output);
      });

      that.stompClient.subscribe('/topic/news', function (data) {
          console.log("Got news!");
          var news = JSON.parse(data.body);
          var type = news.type;

          if (type === "win") {
            // news.message = winner name
            console.log(news.message);
            that.stompClient.send("/app/game", {}, JSON.stringify({}));
          }
      });

      that.stompClient.send("/app/game", {}, JSON.stringify({}));
      that.stompClient.send("/app/checkLogin", {}, JSON.stringify({"username": ""}));
    });
  }

  handleToggle() {
    this.setState({ open: !this.state.open });
  }

  handleChange(change, e) {
    this.setState({
      code: change,
    });
  };

  handleSubmitCode(e) {
    e.preventDefault();
    this.stompClient.send("/app/pm", {}, JSON.stringify({"code": this.state.code }));
    console.log("Submit username \"" + this.state.code + "\"");
  }

  render() {
    let username = this.state.username;
    let challenge = this.state.challenge;

    return (
      <div className="gameContainer">
        <h1>Challenge - {challenge.name}</h1>
        <h3>Welcome {username}</h3>
        <p>{"Hint (will be removed):"}<br/>
		        {""}
        </p>
        <RaisedButton
          label="Toggle"
          onTouchTap={this.handleToggle}/>
        <Drawer open={this.state.open}>
          {challenge.description}
        </Drawer>

        <Editor
        code={this.state.code}
        onChange={this.handleChange}
        onSubmit={this.handleSubmitCode}/>
        <Output output={this.state.output}/>
      </div>
    );
  }
}

Game.contextTypes = {
  store: React.PropTypes.object,
};
