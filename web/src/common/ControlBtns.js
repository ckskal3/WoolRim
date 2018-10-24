import React, { Component } from 'react'
import { Button, ButtonGroup } from '@blueprintjs/core';
import { Link } from 'react-router-dom';

class ControlBtns extends Component {
  constructor(props) {
    super(props);
    const { title, match } = props
    this.state = {
      title,
      path: match.path + '/create',
      isActive: true,
    }
  }

  toggle = () => {
    const { isActive } = this.state;
    const { match } = this.props;
    this.setState({
      isActive: !isActive,
      path: isActive ? match.path : match.path + '/create',
    })
  }
  render() {
    const { title, path } = this.state;
    const { onApply } = this.props;
    return (
      <ButtonGroup>
        <Link to={path} style={{ textDecoration: 'none' }}>
          <Button icon='annotation' text={`${title} 작성`} onClick={this.toggle}/>
        </Link>
        <Button rightIcon="arrow-right" intent="success" text={`${title} DB에 적용`} onClick={onApply} />
      </ButtonGroup>
    )
  }
}

export default ControlBtns;
