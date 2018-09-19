import React, { Component } from 'react';
import { Poem } from '../../components';
import axios from 'axios';

export class PoemContainer extends Component {
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
      getAllPoem{
        id
        name
        poet_id
        content
        auth_count
        point
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
      <Poem data={data}/>
    );
  }
}