import React, { Component } from 'react';
import { FormGroup, InputGroup } from "@blueprintjs/core";

class NoticeRegister extends Component {
  constructor(props){
    super(props);
    console.log(props.match);
  }
  render () {
    return (
      <div>
        <FormGroup label='공지 내용'>
          <InputGroup/>
        </FormGroup>
        <hr/>
      </div>
    )
  }
}

export default NoticeRegister