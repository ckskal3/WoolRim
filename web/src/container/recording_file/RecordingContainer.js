import React, { Component } from 'react';
import { Recording } from '../../components';
import axios from 'axios';

export class RecordingContainer extends Component {
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
      getAllRecording {
        id
        path
        auth_flag
        user_id
        duration
        poem_id
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
      <Recording data={data}/>
    );
  }
}