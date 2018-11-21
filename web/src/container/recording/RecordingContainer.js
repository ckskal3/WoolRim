import React, { Component } from 'react';
import { Route } from 'react-router-dom';

import { RecordingTable } from '../../components';
import ControlBtns from '../../common/ControlBtns';
import RecordingRegister from '../../components/recording/RecordingRegister';
import { getAllRecording, deleteRecording } from './RecordingQueries';
import '../Container.css';

export class RecordingContainer extends Component {
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
    const result = await getAllRecording();
    this.setState({
      data: result,
    })
  }

  onDelete = async (input) => {
    if(window.confirm('정말로 삭제 하시겠습니까?')){
      if(await deleteRecording(input.id)){
        window.alert('삭제완료');
      }else{
        window.alert('삭제실패');
      }
    }
    await this.getData()
  }

  render() {
    const { data, toDeleteDataList, toCreateDataList } = this.state;
    return (
      <div className='main'>
        <ControlBtns title='녹음파일' match={this.props.match} onApply={this.onDBApply} />
        <hr />
        <Route path='/recording/create' render={() => <RecordingRegister onRegister={this.onCreate} />} />
        <Route path='/recording' render={() =>
          <RecordingTable data={data}
            toDeleteDataList={toDeleteDataList}
            onDelete={this.onDelete}
          />}
        />
      </div>
    );
  }
}