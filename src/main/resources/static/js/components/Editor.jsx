import React, { Component } from 'react';
import { Link } from 'react-router';
import MonacoEditor from 'react-monaco-editor';
import RaisedButton from 'material-ui/RaisedButton';

export default class Editor extends Component {
  constructor(props) {
    super(props);
    this.editorDidMount = this.editorDidMount.bind(this);
  }

  editorDidMount(editor, monaco) {
    console.log('editorDidMount', editor);
    editor.focus();
  }

  render() {
    const options = {
      selectOnLineNumbers: true
    };

    return (
      <div>
        <MonacoEditor
          width="100%"
          height="600"
          language="java"
          options={options}
          value={this.props.code}
          onChange={this.props.onChange}
          editorDidMount={this.editorDidMount}
        />
      <form onSubmit={this.props.onSubmit}>
        <RaisedButton className="submitCode" label="Submit" type="submit"/>
      </form>
      </div>
    );
  }
}
