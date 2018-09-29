import React, { Component } from 'react'
import { Button } from 'reactstrap'

class ModifyBtn extends Component {
  constructor(props) {
    super(props);
    this.state = {
      cancelable: false,
    }
  }
  onClick = () => {
    const { cancelable } = this.state;
    this.setState({
      cancelable: !cancelable
    })
  }
  render() {
    const { cancelable } = this.state;
    return (
      <span>
        {cancelable ?
          <Button color='secondary' onClick={this.onClick}>취소</Button> :
          <Button color='info' onClick={this.onClick}>수정</Button>}
      </span>
    )
  }
}

export default ModifyBtn