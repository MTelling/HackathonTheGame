import React, { Component } from 'react';
import { Link } from 'react-router';
import { Tabs, Tab } from 'material-ui/Tabs';
import LeaderBoard from './LeaderBoard';
import nl2br from 'react-newline-to-break';


export default class Output extends Component {
    constructor(props) {
        super(props);
    }


    render() {
        return (
            <Tabs tabItemContainerStyle={{backgroundColor:"#FFD54F"}}
                  inkBarStyle={{backgroundColor: "#EF5350"}}
                  style={{backgroundColor: this.props.activeTab==="output"?"black":"#E0F2F1", overflow:"hidden"}}
                  onChange={this.props.onChange}
                  value={this.props.activeTab}>

                <Tab label="Challenge" value="description" style={{color:"black"}} description={this.props.description}>
                    <div className="description">
                        Goal: {nl2br(this.props.description)}
                    </div>
                </Tab>

                <Tab label="Leaderboard" value="lb" style={{color:"black"}} gameState={this.props.gameState}>
                    <LeaderBoard hide={true} gameState={this.props.gameState}/>
                </Tab>

                <Tab label="Output" value="output" style={{color:"#283593"}} output={this.props.output}>
                    <div className="output">
                        {nl2br(this.props.output)}
                    </div>
                </Tab>
            </Tabs>
        );
    }
}
