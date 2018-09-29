import React, { Component } from 'react'
import { Button } from 'reactstrap'

class RemoveBtn extends Component {
  constructor(props) {
    super(props);
    const { id } = props;
    this.state = {
      id,
      cancelable: false,
    }
  }

  onClick = () => {
    const { onRemoveToggle } = this.props;
    const { cancelable } = this.state;
    onRemoveToggle(this.state.id);
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
          <Button color='danger' onClick={this.onClick}>삭제</Button>}
      </span>
    )
  }
}

export default RemoveBtn