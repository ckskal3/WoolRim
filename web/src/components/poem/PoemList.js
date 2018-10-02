import React, { Component } from 'react'
import { Table } from 'reactstrap';
import './PoemList.css'
import RemoveBtn from '../../common/RemoveBtn';
import ModifyBtn from '../../common/ModifyBtn';

export class PoemList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      modifying_list: [],
      modified_value: null,
    }
  }

  toggleModifyingList = (input) => {
    const { modifying_list } = this.state;
    if (modifying_list.includes(input)) {
      this.setState({
        modifying_list: modifying_list.filter(val => val !== input),
      })
    } else {
      this.setState({
        modifying_list: modifying_list.concat(input),
      })
    }
  }

  onModifyValue = (e) => {
    const value = {
      name: e.id === 'name' ? e.target.value : undefined,
      content: e.id === 'content' ? e.target.value : undefined,
      auth_count: e.id === 'auth_count' ? e.target.value : undefined,
      point: e.id === 'point' ? e.target.value : undefined,
      poet: e.id === 'poet' ? e.target.value : undefined,
      length: e.id === 'length' ? e.target.value : undefined,
    }
    this.setState({
      modified_value: value,
    })
  }

  passModifyValue = (id) => {
    const { onModify } = this.props;
    const { modified_value } = this.state;
    onModify({ id, ...modified_value });
  }

  render() {
    const { onRemove, onEnterRecord, data, regitFlag } = this.props;
    const { modifying_list } = this.state;
    let contents;
    let key = 0;
    if (data) {
      contents = data.map(data => {
        key = data.id === 'NEW' ? Number(key) + 1 : data.id;
        const buttons = (<td>
          <RemoveBtn id={data.id} onRemoveToggle={onRemove} />
          &nbsp;&nbsp;&nbsp;
          <ModifyBtn data={data} onModifyToggle={this.passModifyValue} toggleModifying={this.toggleModifyingList} />
        </td>);
        const isModify = modifying_list.includes(data.id);
        if (isModify) {
          return (
            <tr key={key}>
              <th scope="row">{data.id}</th>
              <td><input id='name' type='text' placeholder={data.name} onChange={this.onModifyValue} /></td>
              <td><input id='content' type='text' placeholder={data.content} onChange={this.onModifyValue} /></td>
              <td><input id='auth_count' type='text' placeholder={data.auth_count} onChange={this.onModifyValue} /></td>
              <td><input id='point' type='text' placeholder={data.point} onChange={this.onModifyValue} /></td>
              <td><input id='poet' type='text' placeholder={data.poet.name} onChange={this.onModifyValue} /></td>
              <td><input id='length' type='text' placeholder={data.length} onChange={this.onModifyValue} /></td>
              {buttons}
            </tr>
          )
        } else {
          return (
            <tr key={key}>
              <th scope="row">{data.id}</th>
              <td>{data.name}</td>
              <td>{data.content}</td>
              <td>{data.auth_count}</td>
              <td>{data.point}</td>
              <td>{data.poet.name}</td>
              <td>{data.length}</td>
              {buttons}
            </tr>
          )
        }
      })
    } else {
      contents = null;
    }

    let addForm = null;
    if (regitFlag) {
      addForm = (
        <tr>
          <th scope="row">NEW</th>
          <td><input id='name' type='text' placeholder='시 제목을 입력하세요' onChange={onEnterRecord} /></td>
          <td><input id='content' type='text' placeholder='내용을 입력하세요' onChange={onEnterRecord} /></td>
          <td><input id='auth_count' type='text' placeholder='시 카운트을 입력하세요' onChange={onEnterRecord} /></td>
          <td><input id='point' type='text' placeholder='봉사 점수을 입력하세요' onChange={onEnterRecord} /></td>
          <td><input id='poet' type='text' placeholder='시인을 입력하세요' onChange={onEnterRecord} /></td>
          <td><input id='length' type='text' placeholder='길이를 입력하세요' onChange={onEnterRecord} /></td>
          <td></td>
        </tr>
      )
    }
    return (
      <div>
        <Table className='table'>
          <thead>
            <tr>
              <th>id</th>
              <th>이름</th>
              <th>내용</th>
              <th>카운트</th>
              <th>봉사 점수</th>
              <th>시인</th>
              <th>길이</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {contents}
            {addForm}
          </tbody>
        </Table>
      </div>
    )
  }
}
