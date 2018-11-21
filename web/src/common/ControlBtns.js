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
    return (
      <ButtonGroup>
        <Link to={path} style={{ textDecoration: 'none' }}>
          <Button icon='annotation' text={`${title} 작성`} onClick={this.toggle}/>
        </Link>
      </ButtonGroup>
    )
  }
}

export default ControlBtns;
