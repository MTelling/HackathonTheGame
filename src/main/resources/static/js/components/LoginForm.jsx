import React, { Component } from 'react';
import { Link } from 'react-router';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

export default class LoginForm extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <form onSubmit={this.props.onSubmit}>
              <TextField
                  hintText="besthacker123"
                  floatingLabelText="Username"
                  floatingLabelStyle={{fontWeight:"normal"}}
                  value={this.props.value}
                  onChange={(e)=>{ this.props.onChange(e); }}/>
              <RaisedButton className="loginButton" label="Login" type="submit"/>
            </form>
        );
    }
}

LoginForm.propTypes = {
    onChange: React.PropTypes.func.isRequired,
    onSubmit: React.PropTypes.func.isRequired,
};
