import React, { Component } from 'react';
import { Link } from 'react-router';
import MonacoEditor from 'react-monaco-editor';
import RaisedButton from 'material-ui/RaisedButton';

export default class Editor extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectOnLineNumbers: true,
      autoSize: true,
      automaticLayout: true,
    };
    this.editorDidMount = this.editorDidMount.bind(this);
  }

  editorDidMount(editor, monaco) {
    console.log('editorDidMount', editor);
    editor.focus();
  }

  render() {
    return (
      <div className="editorContainer">
        <MonacoEditor
          width={this.props.width}
          height="500"
          language="java"
          options={this.state}
          value={this.props.code}
          onChange={this.props.onChange}
          editorDidMount={this.editorDidMount}
        />
      </div>
    );
  }
}
