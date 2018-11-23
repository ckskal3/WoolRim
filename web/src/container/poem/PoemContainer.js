import React, { Component } from 'react';
import { Route } from 'react-router-dom';

import { PoemTable } from '../../components';
import ControlBtns from '../../common/ControlBtns';
import PoemRegister from '../../components/poem/PoemRegister';
import { getAllPoem, deletePoem, createPoem, updatePoem } from './PoemQueries';
import '../Container.css';

export class PoemContainer extends Component {
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
    const result = await getAllPoem();
    this.setState({
      data: result,
    })
  }

  onCreate = async (input) => {
    if (!input) {
      return;
    }
    delete input.poet;
    if(await createPoem(input)){
      window.alert('생성완료');
    }else{
      window.alert('생성실패');
    }
    await this.getData();
  }

  onDelete = async (input) => {
    if(window.confirm('정말로 삭제 하시겠습니까?')){
      if(await deletePoem(input.id)){
        window.alert('삭제완료')
      }else{
        window.alert('삭제실패')
      }
    }
    await this.getData();
  }

  onUpdate = async (input) => {
    if(await updatePoem({
      id: input.id,
      name: input.name,
      content: input.content,
      point: input.point,
      length: input.length,
    })){
    }else{
      window.alert('수정실패');
    }
  }

  render() {
    const { data } = this.state;
    return (
      <div className='main'>
        <ControlBtns title='시' match={this.props.match} onApply={this.onDBApply} />
        <hr />
        <Route path='/poem/create' render={() => <PoemRegister onRegister={this.onCreate} />} />
        <Route path='/poem' render={() =>
          <PoemTable data={data}
            onDelete={this.onDelete}
            onUpdate={this.onUpdate}
          />}
        />
      </div>
    );
  }
}