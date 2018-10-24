import React, { Component } from 'react';
import { Button, Intent } from '@blueprintjs/core';

class Remover extends Component {
  
  onBtnClick = () => {
    const { data } = this.props;
    this.props.onClick(data)
  }
  render () {
    return (
      <Button intent={Intent.DANGER}
        onClick={this.onBtnClick}
        minimal='true'
        small='true'>
        삭제하기
      </Button>
    )
  }
}

export default Remover