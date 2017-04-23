import React, { Component } from 'react';
import { Link } from 'react-router';
import FlatButton from 'material-ui/FlatButton';
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from 'material-ui/Table';
import ContentInbox from 'material-ui/svg-icons/content/inbox';
import Paper from 'material-ui/Paper';

export default class LeaderBoard extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    const style= [
      { width: "20%" },
      { width: "40%" },
      { width: "40%" },
    ];

    let gameState = this.props.gameState;
    console.log("LB:", gameState);
    if(!gameState.lb.length){
      return (
        <div className="description">Currently, there is no user data.</div>
      );
    }
    return (
      <Table>
          {()=>{
            if(!this.props.hide)
              return (
                <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                  <TableRow>
                    <TableHeaderColumn style={style[0]}></TableHeaderColumn>
                    <TableHeaderColumn className="tableHeader" style={style[1]}>Username</TableHeaderColumn>
                    <TableHeaderColumn className="tableHeader" style={style[2]}>Score</TableHeaderColumn>
                  </TableRow>
                </TableHeader>
              );
          }}
        <TableBody displayRowCheckbox={false}>
          {gameState.lb.map((user, idx)=>{
            console.log(idx);
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
    );
  }
}
