import React, { Component } from 'react'
import { Route } from 'react-router-dom';
import Header from './common/Header'
import axios from 'axios';
import { FormGroup, InputGroup, Card, Intent, Tooltip, Button, Toaster } from '@blueprintjs/core';
import { UserContainer, PoetContainer, PoemContainer, NoticeContainer, RecordingContainer } from './container'

import './LoginView.css';
import serverInfo from './serverInfo';
class LoginView extends Component {
  toastRef = null;
  toastSuccessRef = null;

  constructor(props) {
    super(props);
    this.state = {
      auth: false,
      showPassword: false,
      passwd: '',
      id: '',
      userInfo: null,
    }
  }

  onLogin = async () => {
    const { id, passwd } = this.state;
    const result = (await axios.post(serverInfo.webServerURL + '/login', {
      id,
      passwd,
    })).data;
    
    if (!result.isSuccess) {
      this.toastRef.show({
        icon: 'warning-sign',
        intent: Intent.DANGER,
        message: '로그인에 실패하였습니다.',
        timeout: 3000,
      })
      return;
    }

    this.setState({
      auth: true,
      userInfo: result.user,
    })
  }

  onInputId = (e) => {
    this.setState({
      id: e.target.value,
    })
  }

  onInputPasswd = (e) => {
    this.setState({
      passwd: e.target.value,
    })
  }

  handleLockClick = () => {
    const { showPassword } = this.state;
    this.setState({
      showPassword: !showPassword,
    })
  }

  handleKeyPress = (e) => {
    if (e.charCode == 13) {
      this.onLogin();
    }
  }


  render() {
    const { auth, userInfo } = this.state;
    if (auth) {
      return (
        <div>
          <Header user={userInfo} />
          <Route path="/user" component={UserContainer} />
          <Route path="/poet" component={PoetContainer} />
          <Route path="/poem" component={PoemContainer} />
          <Route path="/notice" render={props => <NoticeContainer {...props} current_account={userInfo}/>} />
          <Route path="/recording" component={RecordingContainer} />
        </div>
      )
    } else {
      const { showPassword, passwd, id } = this.state;
      const lockButton = (
        <Tooltip content={`비밀번호 ${showPassword ? "숨기기" : "보이기"}`} >
          <Button
            icon={showPassword ? "unlock" : "lock"}
            intent={Intent.WARNING}
            minimal={true}
            onClick={this.handleLockClick}
          />
        </Tooltip>
      );
      return (
        <div className='root'>
          <div className='logo'>
            <img src='./logo.png' />
          </div>
          <Card className='login_card'>
            <FormGroup label='아이디'>
              <InputGroup type='text' value={id} placeholder='아이디를 입력하세요' onChange={this.onInputId} />
            </FormGroup>
            <FormGroup label='비밀번호'>
              <InputGroup type={showPassword ? "text" : "password"} value={passwd} placeholder='비밀번호를 입력하세요' rightElement={lockButton} onChange={this.onInputPasswd} onKeyPress={this.handleKeyPress} />
            </FormGroup>
            <div className='login_btn'>
              <Button text='로그인' intent='primary' onClick={this.onLogin} />
            </div>
          </Card>
          <Toaster ref={ref => { this.toastRef = ref }} />
        </div>
      )
    }
  }
}

export default LoginView