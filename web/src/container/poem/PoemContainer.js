import React, { Component } from 'react';
import { PoemList } from '../../components';
import { getAllPoem, deletePoem, createPoem, updatePoem } from './PoemQueries';

export class PoemContainer extends Component {
  constructor(props) {
    super(props)
    this.state = {
      poemList: [],
    }
  }

  componentDidMount() {
    this.getData()
  }

  getData = async () => {
    const data = await getAllPoem();
    this.setState({
      poemList: data,
    })
  }

  render() {
    const { poemList } = this.state;
    return (
      <PoemList data={poemList}/>
    );
  }
}