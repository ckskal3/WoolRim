import React, { Component } from 'react';
import { Button, FormGroup, Intent, TextArea } from "@blueprintjs/core";

class PoetRegister extends Component {
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
        <FormGroup label='시인 이름'>
          <TextArea
            value={this.state.name}
            onChange={this.onNameChange} 
            fill='true'/>
        </FormGroup>
        <Button text='등록하기'
          intent={Intent.PRIMARY}
          onClick={this.onSubmit} />
        <hr />
      </div>
    )
  }
}

export default PoetRegister