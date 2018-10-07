import React from 'react'
import { Button, ButtonGroup } from '@blueprintjs/core';
import { Link } from 'react-router-dom';
const ControlBtns = ({title, match, onApply, onCreate}) => {
  return (
    <div>
      <ButtonGroup>
        <Link to={match.path + '/create'} style={{textDecoration:'none'}}>
          <Button icon='annotation' text={`${title} 작성`} onClick={onCreate} />
        </Link>
        <Button rightIcon="arrow-right" intent="success" text={`${title} DB에 적용`} onClick={onApply} />
      </ButtonGroup>
    </div>
  )
}

export default ControlBtns;