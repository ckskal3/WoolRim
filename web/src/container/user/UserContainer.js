import React, { Component } from 'react';
import { UserTable } from '../../components';
import { getAllUser } from './UserQueries';

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

  getData = async () => {
    const result = await getAllUser();
    this.setState({
      data: result,
    })
  }

  render() {
    const { data } = this.state;
    return (
      <UserTable data={data}/>
    );
  }
}