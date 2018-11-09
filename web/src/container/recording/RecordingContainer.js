import React, { Component } from 'react';
import { Route } from 'react-router-dom';

import { RecordingTable } from '../../components';
import ControlBtns from '../../common/ControlBtns';
import RecordingRegister from '../../components/recording/RecordingRegister';
import { getAllRecording, deleteRecording, createRecording } from './RecordingQueries';
import '../Container.css';

export class RecordingContainer extends Component {
  constructor(props) {
    super(props)
    this.state = {
      data: [],
      toCreateDataList: [],
      toDeleteDataList: [],
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

  onDBApply = async () => {
    const { toDeleteDataList, toCreateDataList, toUpdateDataList, data } = this.state;
    if (toCreateDataList.length === 0 &&
      toDeleteDataList.length === 0) {
      window.alert('적용 할 내용 없음');
      return;
    }
    const msg = `
      ${toCreateDataList.length} 개 생성
      ${toDeleteDataList.map(v => `${v}번 `)} 삭제`
    const confirm = window.confirm(msg)

    if (!confirm) {
      return;
    }
    if (toCreateDataList.length > 0) {
      toCreateDataList.map(v => {
        if (v.key) {
          delete v.key;
        }
      })
      await createRecording(toCreateDataList);
    }
    if (toDeleteDataList.length > 0) {
      await deleteRecording(toDeleteDataList);
    }
    
    await this.getData();

    this.setState({
      toCreateDataList: [],
      toDeleteDataList: [],
    })
  }

  onCreate = (input) => {
    if (!input) {
      return;
    }
    const { data, toCreateDataList } = this.state;
    const date = new Date();
    this.setState({
      toCreateDataList: toCreateDataList.concat({
        name: input.name,
        key: date,
      }),
      data: data.concat({ id: 'NEW', ...input, key: date }),
    })
  }

  onDelete = (input) => {
    if (input.key) {
      const { toCreateDataList, data } = this.state;
      this.setState({
        toCreateDataList: toCreateDataList.filter(v => {
          return v.key !== input.key
        }),
        data: data.filter(v => {
          return v.key !== input.key
        }),
      });
      return;
    }
    const { toDeleteDataList } = this.state;
    if (toDeleteDataList.includes(input.id)) {
      this.setState({
        toDeleteDataList: toDeleteDataList.filter(v => v !== input.id),
      })
      return;
    }
    this.setState({
      toDeleteDataList: toDeleteDataList.concat(input.id),
    })
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