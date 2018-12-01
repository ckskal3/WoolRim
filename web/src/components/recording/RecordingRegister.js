import React, { Component } from 'react';
import { Button, FormGroup, Intent, TextArea } from "@blueprintjs/core";

class RecordingRegister extends Component {
  constructor(props) {
    super(props);
    this.state = {
      name: '',
    }
  }

  onSubmit = () => {
    const { name } = this.state;
    this.props.onRegister({name});
    this.setState({
      name: '',
    })
  }

  onNameChange = (event) => {
    this.setState({
      name: event.target.value,
    });
  }

  render() {
    return (
      <div>
        <h1>현재는 지원하지 않습니다.</h1>
        < hr/>
      </div>
    )
  }
}

export default RecordingRegister