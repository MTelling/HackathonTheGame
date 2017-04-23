import React, { Component } from 'react';
import { Link } from 'react-router';
import Paper from 'material-ui/Paper';
import { Tabs, Tab } from 'material-ui/Tabs';

export default class Output extends Component {
  constructor(props) {
    super(props);
    this.state = {
      tab: "description"
    };
    this.handleChangeTab = this.handleChangeTab.bind(this);
  }

  handleChangeTab(val){
    if(val!="submit"){
      this.setState({
        tab: val,
      });
    }
  };

  render() {
    return (
        <Paper className="outputContainer" zDepth={2} rounded={false}>
          <Tabs value={this.state.tab} onChange={this.handleChangeTab}
            tabItemContainerStyle={{backgroundColor:"#FFD54F"}}
            inkBarStyle={{backgroundColor: "#EF5350"}}
            style={{height: "100%", backgroundColor: this.state.tab=="output"?"black":"#E0F2F1"}}>
            <Tab label="Challenge" value="description" style={{color:"black"}}>
              <div className="description">
                Goal: {this.props.description}
              </div>
            </Tab>
            <Tab label="Output" value="output" style={{color:"black"}}>
              <div className="output">
                {this.props.output}
              </div>
            </Tab>
            <Tab label="Submit" value="submit" onActive={this.props.onSubmit} style={{color:"#303F9F"}}>
            </Tab>
          </Tabs>
        </Paper>
    );
  }
}
