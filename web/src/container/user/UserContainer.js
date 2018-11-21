import React, { Component } from 'react';
import { UserTable } from '../../components';
import { getAllUser, deleteUser } from './UserQueries';

export class UserContainer extends Component {
  constructor(props) {
    super(props)
    this.state = {
      data: [],
    }
  }

  componentDidMount() {
    this.getData()
  }

  onDelete = async (input) => {
    if(window.confirm('정말로 삭제 하시겠습니까')){
      if(await deleteUser(input.id)){
        window.alert('삭제완료');
      }else{
        window.alert('삭제실패');
      }
    }
    await this.getData();
  }

  getData = async () => {
    const result = await getAllUser();
    this.setState({
      data: result,
    })
  }

  render() {
    const { data } = this.state;
    return (
      <UserTable data={data} onDelete={this.onDelete}/>
    );
  }
}