import React, { Component } from 'react';
import { Button, FormGroup, InputGroup, Intent, TextArea } from "@blueprintjs/core";

class NoticeRegister extends Component {
  constructor(props) {
    super(props);
    this.state = {
      content: '',
    }
  }

  onSubmit = () => {
    const { content } = this.state;
    this.props.onRegister({content});
    this.setState({
      content: '',
    })
  }

  onContentChange = (event) => {
    this.setState({
      content: event.target.value,
    });
  }

  render() {
    return (
      <div>
        <FormGroup label='공지 내용'>
          <TextArea
            value={this.state.content}
            onChange={this.onContentChange} 
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

export default NoticeRegister