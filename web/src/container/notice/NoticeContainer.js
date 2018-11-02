import React, { Component } from 'react';
import { Route } from 'react-router-dom';

import { NoticeTable } from '../../components';
import '../Container.css'
import ControlBtns from '../../common/ControlBtns';
import NoticeRegister from '../../components/notice/NoticeRegister';
import { getAllNotice, deleteNotice, createNotice, updateNotice } from './NoticeQueries';
import { dateFormatter } from '../../common/Tools';

export class NoticeContainer extends Component {
  constructor(props) {
    super(props)
    this.state = {
      data: [],
      toCreateDataList: [],
      toDeleteDataList: [],
      toUpdateDataList: [],
    }
  }

  onCreate = (content) => {
    if (!content) {
      return;
    }
    const { data, toCreateDataList } = this.state;
    const { current_account } = this.props;
    const date = new Date();
    const inputData = {
      id: 'NEW',
      content,
      date: dateFormatter(date),
      user: { name: current_account.name },
      key: date,
    }
    
    this.setState({
      toCreateDataList: toCreateDataList.concat({
        content: inputData.content,
        user_id: current_account.id,
        key: date,
      }),
      data: data.concat(inputData),
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

  onUpdate = (input) => {
    const { data } = this.state;
    if (input.key) {
      const { toCreateDataList } = this.state;
      this.setState({
        toCreateDataList: toCreateDataList.map(v => {
          if (v.key === input.key) {
            return {
              content: input.content,
            };
          }
          return v;
        }),
        data: data.map(v => {
          if (v.key === input.key) {
            v = input;
          }
          return v;
        }),
      });
      return;
    }
    const { toUpdateDataList } = this.state;
    if (!toUpdateDataList.includes(input.id)) {
      this.setState({
        toUpdateDataList: toUpdateDataList.concat(input.id),
      })
    }
    this.setState({
      data: data.map(v => {
        if (v.id === input.id) {
          return input;
        }
        return v;
      }),
    })
  }

  onDBApply = async () => {
    const { toDeleteDataList, toCreateDataList, toUpdateDataList, data } = this.state;
    if(toCreateDataList.length === 0 &&
      toDeleteDataList.length === 0 &&
      toUpdateDataList.length === 0){
        window.alert('적용 할 내용 없음');
        return;
      }
    const msg = `
      ${toCreateDataList.length} 개 생성
      ${toDeleteDataList.map(v => `${v}번 `)} 삭제
      ${toUpdateDataList.map(v => `${v}번 `)} 업데이트
    `
    const confirm = window.confirm(msg)

    if (!confirm) {
      return;
    }
    if(toCreateDataList.length > 0) {
      toCreateDataList.map(v => {
        if(v.key){
          delete v.key;
        }
      })
      await createNotice(toCreateDataList);
    }
    if(toDeleteDataList.length > 0) {
      await deleteNotice(toDeleteDataList);
    }
    if(toUpdateDataList.length > 0) {
      const filteredData = data.filter(v => {
        return toUpdateDataList.includes(v.id);
      }) 
      await updateNotice(filteredData.map(v => {
        return {id: v.id, content: v.content};
      }));
    }
    await this.getData();
    this.setState({
      toCreateDataList: [],
      toDeleteDataList: [],
      toUpdateDataList: [],
    })
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