import React, { Component } from 'react'
import { Button } from 'reactstrap'

class ModifyBtn extends Component {
  constructor(props) {
    super(props);
    const { data } = props;
    this.state = {
      data,
      cancelable: false,
    }
  }

  onConfirm = () => {
    const { onModifyToggle, toggleModifying } = this.props;
    const { data, cancelable } = this.state;
    toggleModifying(data.id)
    onModifyToggle(data.id);
    this.setState({
      cancelable: !cancelable
    })
  }

  onClick = () => {
    const { toggleModifying } = this.props;
    const { data, cancelable } = this.state;
    toggleModifying(data.id)
    this.setState({
      cancelable: !cancelable
    })
  }

  render() {
    const { cancelable } = this.state;
    return (
      <span>
        {cancelable ?
          <Button color='primary' onClick={this.onConfirm}>확인</Button> :
          <Button color='info' onClick={this.onClick}>수정</Button>}
      </span>
    )
  }
}

export default ModifyBtn