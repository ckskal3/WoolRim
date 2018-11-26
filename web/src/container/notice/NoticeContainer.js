import React, { Component } from 'react';
import { Route } from 'react-router-dom';

import { NoticeTable } from '../../components';
import '../Container.css'
import ControlBtns from '../../common/ControlBtns';
import NoticeRegister from '../../components/notice/NoticeRegister';
import { getAllNotice, deleteNotice, createNotice, updateNotice } from './NoticeQueries';
export class NoticeContainer extends Component {
  constructor(props) {
    super(props)
    this.state = {
      data: [],
    }
  }

  onCreate = async (input) => {
    input.user_id = 1; // 관리자 전용으로 만들어야함
    if (!input) {
      return;
    }
    if(await createNotice(input)){
      window.alert('생성완료');
    }else{
      window.alert('생성실패');
    }
    await this.getData()
  }

  onDelete = async (input) => {
    if(window.confirm('정말로 삭제 하시겠습니까?')){
      if(await deleteNotice(input.id)){
        window.alert('삭제완료');
      }else{
        window.alert('삭제실패');
      }
    }
    await this.getData()
  }

  onUpdate = async (input) => {
    input.user_id = 1; // 관리자 전용으로 만들어야함
    console.log(input)
    if(await updateNotice({
      id: input.id,
      content: input.content,
    })){
      window.alert('수정완료');
    }else{
      window.alert('수정실패');
    }
  }

  componentDidMount() {
    this.getData()
  }

  getData = async () => {
    const result = await getAllNotice();
    this.setState({
      data: result,
    })
  }

  render() {
    const { data, toDeleteDataList, toCreateDataList, toUpdateDataList } = this.state;
    return (
      <div className='main'>
        <ControlBtns title='공지사항' match={this.props.match} onApply={this.onDBApply} />
        <hr />
        <Route path='/notice/create' render={() => <NoticeRegister onRegister={this.onCreate} />} />
        <Route path='/notice' render={() =>
          <NoticeTable data={data}
            toDeleteDataList={toDeleteDataList}
            onDelete={this.onDelete}
            onUpdate={this.onUpdate}
          />} 
        />
      </div>
    );
  }
}