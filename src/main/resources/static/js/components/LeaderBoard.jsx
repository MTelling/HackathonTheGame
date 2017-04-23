import React, { Component } from 'react';
import { Link } from 'react-router';
import FlatButton from 'material-ui/FlatButton';
import Dialog from 'material-ui/Dialog';
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from 'material-ui/Table';
import ContentInbox from 'material-ui/svg-icons/content/inbox';

export default class LeaderBoard extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    let gameState = this.props.gameState;

    const actions = [
      <FlatButton
        label="Exit"
        primary={true}
        onTouchTap={this.props.onClose}
      />,
      <FlatButton
        label="Next Challenge"
        primary={true}
        disabled={!gameState.ready}
        onTouchTap={this.props.onClose}
      />,
    ];
    const style= [
      { width: "20%" },
      { width: "40%" },
      { width: "40%" },
    ];

    return (
      <Dialog
        title="Game Over"
        actions={actions}
        modal={true}
        open={gameState.over}>
        <span style={{color:"#EF5350"}}>{gameState.winner}</span> has won the challenge!
        <br/>
        <br/>
        <div style={{textAlign: "center", fontSize: 20}}>Leaderboard</div>
        <Table>
          <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
            <TableRow >
              <TableHeaderColumn style={style[0]}></TableHeaderColumn>
              <TableHeaderColumn className="tableHeader" style={style[1]}>Username</TableHeaderColumn>
              <TableHeaderColumn className="tableHeader" style={style[2]}>Score</TableHeaderColumn>
            </TableRow>
          </TableHeader>
          <TableBody displayRowCheckbox={false}>
            {gameState.lb.map((user)=>{
              return (
                <TableRow key={user.name}>
                  <TableRowColumn style={style[0]}><ContentInbox/></TableRowColumn>
                  <TableRowColumn style={style[1]}>{user.name}</TableRowColumn>
                  <TableRowColumn style={style[2]}>{user.score}</TableRowColumn>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </Dialog>
    );
  }
}
