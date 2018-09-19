import React, { Component } from 'react';
import { User } from '../../components';
import axios from 'axios';

export class UserContainer extends Component {
  constructor(props) {
    super(props)
    this.state = {
      data: null,
    }
  }

  componentDidMount() {
    this.getData()
  }

  getData = async () => {
    const query = `query {
      getAllUser {
        id
        name
        stu_id
        gender
        bongsa_time
      }
    }`;
    const result = await axios.post('http://localhost:3000/graphql', { query });
    this.setState({
      data: result,
    })
  }

  render() {
    const { data } = this.state;
    return (
      <User data={data}/>
    );
  }
}