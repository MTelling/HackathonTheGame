import React, { Component } from 'react';
import { Link } from 'react-router';
import MonacoEditor from 'react-monaco-editor';


export default class Editor extends Component {
  constructor(props) {
    super(props);
    this.state = {
      code: '// type your code...',
    }
    this.onChange = this.onChange.bind(this);
    this.editorDidMount = this.editorDidMount.bind(this);
  }

  editorDidMount(editor, monaco) {
    console.log('editorDidMount', editor);
    editor.focus();
  }

  onChange(newValue, e) {
    console.log('onChange', newValue, e);
  }

  render() {
    const code = this.state.code;
    const options = {
      selectOnLineNumbers: true
    };
    return (
      <MonacoEditor
        width="100%"
        height="600"
        language="java"
        value={code}
        options={options}
        onChange={this.onChange}
        editorDidMount={this.editorDidMount}
      />
    );
  }
}
