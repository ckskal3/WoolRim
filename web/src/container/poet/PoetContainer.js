import React, { Component } from 'react';
import { Poet } from '../../components';
import axios from 'axios';

export class PoetContainer extends Component {
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
      getAllPoet{
        id
        name
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
      <Poet data={data}/>
    );
  }
}