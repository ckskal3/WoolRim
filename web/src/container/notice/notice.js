import React, { Component } from 'react';
import { Notice } from '../../components';
import axios from 'axios';

export class NoticeContainer extends Component {
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
      getAllNotice{
        id
        content
        date
        user_id
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
      <Notice data={data}/>
    );
  }
}