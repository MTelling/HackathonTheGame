import React, { Component } from 'react';
import { Link } from 'react-router';
import Paper from 'material-ui/Paper';
import { Tabs, Tab } from 'material-ui/Tabs';
import LeaderBoard from './LeaderBoard';

export default class Output extends Component {
  constructor(props) {
    super(props);
    this.state = {
      tab: "description"
    };
    this.handleChangeTab = this.handleChangeTab.bind(this);
  }

  handleChangeTab(val){
    this.setState({
      tab: val,
    });
  };

  render() {
    return (
        <Paper className="outputContainer" zDepth={2} rounded={false}>
          <Tabs value={this.state.tab} onChange={this.handleChangeTab}
            tabItemContainerStyle={{backgroundColor:"#FFD54F"}}
            inkBarStyle={{backgroundColor: "#EF5350"}}
            style={{backgroundColor: this.state.tab=="output"?"black":"#E0F2F1", overflow:"hidden"}}>
            <Tab label="Challenge" value="description" style={{color:"black"}}>
              <div className="description">
                Goal: {this.props.description}
              </div>
            </Tab>
            <Tab label="Leaderboard" value="lb" style={{color:"black"}}>
              <LeaderBoard hide={true} gameState={this.props.gameState}/>
            </Tab>
            <Tab label="Output" value="output" style={{color:"#283593"}}>
              <div className="output">
                {this.props.output}
              </div>
            </Tab>
          </Tabs>
        </Paper>
    );
  }
}
