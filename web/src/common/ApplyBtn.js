import React from 'react';
import { Button } from 'reactstrap';

const ApplyBtn = ({ onConfirm, isDisable }) => {
  return (
    <div>
      <Button color='primary' disabled={isDisable} onClick={onConfirm}>DB에 적용하기</Button>
    </div>
  )
}

export default ApplyBtn;