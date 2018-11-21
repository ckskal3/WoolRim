import React, { Component } from 'react';
import { Route } from 'react-router-dom';

import { PoetTable } from '../../components';
import ControlBtns from '../../common/ControlBtns';
import PoetRegister from '../../components/poet/PoetRegister';
import { getAllPoet, deletePoet, createPoet, updatePoet } from './PoetQueries';
import '../Container.css';

export class PoetContainer extends Component {
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
    const result = await getAllPoet();
    this.setState({
      data: result,
    })
  }

  onCreate = async (input) => {
    if (!input) {
      return;
    }
    if(await createPoet(input)){
      window.alert('생성완료');
    }else{
      window.alert('생성실패');
    }
    await this.getData();
  }

  onDelete = async (input) => {
    if(window.confirm('정말로 삭제 하시겠습니까?')){
      if(await deletePoet(input.id)){
        window.alert('삭제완료');
      }else{
        window.alert('삭제실패');
      }
    }
    await this.getData();
  }

  onUpdate = async (input) => {
    if(await updatePoet(input)){
      window.alert('수정완료');
    }else{
      window.alert('수정실패');
    }
  }

  render() {
    const { data } = this.state;
    return (
      <div className='main'>
        <ControlBtns title='시인' match={this.props.match} onApply={this.onDBApply} />
        <hr />
        <Route path='/poet/create' render={() => <PoetRegister onRegister={this.onCreate} />} />
        <Route path='/poet' render={() =>
          <PoetTable data={data}
            onDelete={this.onDelete}
            onUpdate={this.onUpdate}
          />}
        />
      </div>
    );
  }
}