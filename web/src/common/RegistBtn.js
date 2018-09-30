import React, { Component } from 'react'
import { Button } from 'reactstrap';

class RegistBtn extends Component {
  constructor(props) {
    super(props);
    let btn_name;
    switch (props.root) {
      case 'poet':
        btn_name = '시인 등록';
        break;
    }
    this.state = {
      isActive: false,
      btn_name,
    }
  }

  onRegitClicked = () => {
    const { isActive } = this.state;
    this.props.onRegitToggle(!isActive);
    this.setState({
      isActive: !isActive,
    })
  }

  onRegitCanceled = () => {
    const { isActive } = this.state;
    this.props.onRegitCanceled();    
    this.setState({
      isActive: !isActive,
    })
  }

  render() {
    const {isActive} = this.state;
    const complete_str = isActive ? '완료' : '';
    const btn_color = isActive ? 'success' : 'primary';
    return (
      <div>
        <Button color={btn_color} onClick={this.onRegitClicked}>{this.state.btn_name} {complete_str}</Button>
        &nbsp;&nbsp;&nbsp;
        {isActive? <Button color='warning' onClick={this.onRegitCanceled}>등록 취소</Button> :''}
      </div>
    )
  }
}

export default RegistBtn