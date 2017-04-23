import React, { Component } from 'react';
import { Link } from 'react-router';
import ReactEmoji from 'react-emoji';
import FlatButton from 'material-ui/FlatButton';
import Dialog from 'material-ui/Dialog';
import LeaderBoard from './LeaderBoard';

export default class GameOver extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    let gameState = this.props.gameState;

    const actions = [
      <FlatButton
        label="Exit"
        primary={true}
        onTouchTap={this.props.onExit}
      />,
      <FlatButton
        label="Next Challenge"
        primary={true}
        disabled={!gameState.ready}
        onTouchTap={this.props.onClose}
      />,
    ];

    return (
      <Dialog
        title="Challenge solved"
        actions={actions}
        modal={true}
        open={gameState.over}>
        <span style={{color:"#EF5350"}}>{gameState.winner}</span> has won the challenge!
        <br/>
        <br/>
        <div style={{textAlign: "center", fontSize: 20}}>{ReactEmoji.emojify(":fire: Leaderboard :fire:")}</div>
        <LeaderBoard gameState={gameState}/>
      </Dialog>
    );
  }
}
