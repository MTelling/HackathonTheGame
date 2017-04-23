import React, { Component } from 'react';
import { Link } from 'react-router';
import Avatar from 'material-ui/Avatar';
import Chip from 'material-ui/Chip';
import FontIcon from 'material-ui/FontIcon';
import SvgIconFace from 'material-ui/svg-icons/action/face';
import RaisedButton from 'material-ui/RaisedButton';
import Editor from './Editor';
import Output from './Output';
import LeaderBoard from './LeaderBoard';
import UserInfo from './UserInfo';

export default class Game extends Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      width: "100",
      challenge: {
        name: "Sample Name",
        description: "Sample Description",
        initialCode: "//type your code here...",
      },
      user:{
        open: false,
        username: "Anonymous",
      },
      state: {
        ready: false,
        over: false,
        winner: "",
        lb: [],
      },
      submitting: false,
      code: "",
      output: "Sample Output",
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleUserInfo = this.handleUserInfo.bind(this);
    this.handleSubmitCode = this.handleSubmitCode.bind(this);
    this.handleTestLeaderBoard = this.handleTestLeaderBoard.bind(this);
  }

  componentDidMount(){
    var socket = new SockJS('/htg');
    this.stompClient = Stomp.over(socket);
    this.stompClient.debug = null;

    var that = this;
    this.stompClient.connect({}, function (frame) {
      console.log('Connected: ' + frame);

      that.stompClient.subscribe('/user/queue/game', function (gameResponse) {
          var challengeDescription = JSON.parse(gameResponse.body).challengeDescription;
          console.log("Got game description ("+challengeDescription.name+")!");

          if (that.state.challenge === null || challengeDescription.name !== that.state.challenge.name) {
            that.setState({
              challenge: challengeDescription,
              code: challengeDescription.initialCode +
              "\n//type your code here...\
              \n//return new Object[] {Integer.parseInt(args[0])+Integer.parseInt(args[1])};",
              state: Object.assign({}, that.state.state, { ready: true }),
             });
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
          console.log("Got pm response!");
          that.setState({
            submitting: false,
            output: JSON.parse(pmResponse.body).message,
          })
      });

      that.stompClient.subscribe('/topic/news', function (data) {
          console.log("Got news!");
          var news = JSON.parse(data.body);
          var type = news.type;

          if (type === "win") {
            that.setState({
              state: {
                ready: false,
                over: true,
                winner: news.message,
                lb: news.leaderboard,
              }
            });
            that.stompClient.send("/app/game", {}, JSON.stringify({}));
          }
      });

      that.stompClient.send("/app/game", {}, JSON.stringify({}));
      that.stompClient.send("/app/checkLogin", {}, JSON.stringify({"username": ""}));
    });
  }

  handleChange(change, e) {
    this.setState({
      code: change,
    });
  }

  handleClose() {
    this.setState({
      state: Object.assign({}, this.state.state, { over: false }),
    });
  }

  handleUserInfo(){
    this.setState({
      user: Object.assign({}, this.state.user, { open: !this.state.user.open }),
    });
  }

  handleSubmitCode(e) {
    e.preventDefault();
    this.setState({
      submitting: true,
    });
    this.stompClient.send("/app/pm", {}, JSON.stringify({"code": this.state.code }));
  }

  handleTestLeaderBoard(){
    this.setState({
      state: {
        ready: true,
        over: true,
        winner: "SampleWinner2",
        lb: [
          { name: "SampleWinner1", score: "130" },
          { name: "SampleWinner2", score: "20" },
          { name: "SampleWinner3", score: "7" }
        ],
      }
    });
  }

  render() {
    let user = this.state.user;
    let challenge = this.state.challenge;

    return (
      <div className="gameContainer" style={{width: this.state.width + "%"}}>
        <h1 className="title" style={{position:"relative", margin: "16px 0"}}>
            {challenge.name}
          <div className="chipContainer">
            <Chip onTouchTap={this.handleUserInfo}>
              <Avatar icon={<SvgIconFace/>} />
              {user.username}
            </Chip>
          </div>
        </h1>

        <Editor
          width={this.state.width + "%"}
          code={this.state.code}
          onChange={this.handleChange}/>
        <Output
          output={this.state.output}
          description={challenge.description}
          onSubmit={this.handleSubmitCode}/>
        <LeaderBoard
          gameState={this.state.state}
          onClose={this.handleClose}/>
        <UserInfo
          username={user.username}
          open={user.open}
          onClose={this.handleUserInfo}/>

        <form onSubmit={this.handleSubmitCode}>
          <RaisedButton
            label="Submit" type="submit"
            className="submitCode"
            disabled={this.state.submitting}/>
        </form>

        <RaisedButton
          label="Test LB"
          className="submitCode"
          onTouchTap={this.handleTestLeaderBoard}/>
      </div>
    );
  }
}

Game.contextTypes = {
  store: React.PropTypes.object,
};
