import React, { Component } from 'react';
import { Link } from 'react-router';
import Avatar from 'material-ui/Avatar';
import Chip from 'material-ui/Chip';
import FontIcon from 'material-ui/FontIcon';
import SvgIconFace from 'material-ui/svg-icons/action/face';
import FlatButton from 'material-ui/FlatButton';
import RaisedButton from 'material-ui/RaisedButton';
import Dialog from 'material-ui/Dialog';
import Editor from './Editor';
import Output from './Output';
import GameOver from './GameOver';
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
          score: 0,
      },
      state: {
        ready: false,
        over: false,
        winner: "",
        lb: [],
      },
      reset: false,
      submitting: false,
      code: "",
      output: "Sample Output",
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleUserInfo = this.handleUserInfo.bind(this);
    this.handleSubmitCode = this.handleSubmitCode.bind(this);
    this.handleConfirmReset = this.handleConfirmReset.bind(this);
    this.handleLogout = this.handleLogout.bind(this);
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
            challengeDescription.initialCode+="\n//type your code here...";
            that.setState({
              challenge: challengeDescription,
              code: challengeDescription.initialCode,
              state: Object.assign({}, that.state.state, { ready: true }),
             });
          }
      });

      that.stompClient.subscribe('/user/queue/checkLogin', function (response) {
          var message = JSON.parse(response.body);

          if (message.status === "alreadyIn") {
              that.setState({
                user: {
                    username: message.username,
                    open: false,
                    score: message.score,
                },
                  state: Object.assign({}, that.state.state, {
                      lb: message.leaderboard,
                  }),
              });

          } else {
              console.log("NOT LOGGED IN: " + message.status);
              that.props.router.push("/");
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
            that.stompClient.send("/app/checkLogin", {}, JSON.stringify({"username": ""}));
          }
      });

      setTimeout(function() {
          console.log("Running from timout!");

          that.stompClient.send("/app/game", {}, JSON.stringify({}));
          that.stompClient.send("/app/checkLogin", {}, JSON.stringify({"username": ""}));

      }, 25);

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

  handleConfirmReset(){
    this.setState({
      code: this.state.challenge.initialCode,
      reset: false,
    });
  }

  handleSubmitCode(e) {
    e.preventDefault();
    this.setState({
      submitting: true,
    });
    this.stompClient.send("/app/pm", {}, JSON.stringify({"code": this.state.code }));
  }

  handleLogout() {
    this.stompClient.unsubscribe('/user/queue/checkLogin');
    this.stompClient.unsubscribe('/user/queue/game');
    console.log("unsubscribe to /user/queue/checkLogin and /user/queue/game");
    this.props.router.push('/');
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
                {this.state.user.username}
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
          gameState={this.state.state}
          onSubmit={this.handleSubmitCode}/>
        <GameOver
          gameState={this.state.state}
          onExit={this.handleLogout}
          onClose={this.handleClose}/>
        <UserInfo
          username={this.state.user.username}
          open={this.state.user.open}
          score={this.state.user.score}
          onClose={this.handleUserInfo}/>

        <form onSubmit={this.handleSubmitCode}>
          <RaisedButton
            label="Submit" type="submit"
            className="submitCode"
            disabled={this.state.submitting}/>
        </form>

        <RaisedButton
          label="Reset"
          className="submitCode"
          onTouchTap={()=>this.setState({reset: true})}/>


        <Dialog
            title="Reset your code?"
            actions={[
              <FlatButton
                label="Cancel"
                primary={true}
                onTouchTap={()=>this.setState({reset: false})}/>,
              <FlatButton
                label="Confirm"
                primary={true}
                onTouchTap={this.handleConfirmReset}/>
            ]}
            modal={true}
            open={this.state.reset}>
            Your code will not be saved and your changes cannot be undone.
          </Dialog>
      </div>
    );
  }
}

Game.contextTypes = {
  store: React.PropTypes.object,
};
