import React, { Component } from 'react'
import { Table } from 'reactstrap';
import './PoetList.css'
import RemoveBtn from '../../common/RemoveBtn';
import ModifyBtn from '../../common/ModifyBtn';

export class PoetList extends Component {
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
        modifying_list: modifying_list.filter(val => val!==input),
      })
    } else {
      this.setState({
        modifying_list: modifying_list.concat(input),
      })
    }
  }

  onModifyValue = (e) => {
    this.setState({
      modified_value: e.target.value,
    })
  }

  test = (id) => {
    const { onModify } = this.props;
    const { modified_value } = this.state;
    onModify({id, name: modified_value});
  }


  render() {
    const { onRemove, onEnterRecord, data, addFlag } = this.props;
    const { modifying_list } = this.state;
    let contents;
    let key = 0;
    if (data) {
      contents = data.map(data => {
        key = data.id === 'NEW' ? Number(key) + 1 : data.id;
        const buttons = (<td>
          <RemoveBtn id={data.id} onRemoveToggle={onRemove} />
          &nbsp;&nbsp;&nbsp;
          <ModifyBtn data={data} onModifyToggle={this.test} toggleModifying={this.toggleModifyingList} />
        </td>);
        let table_contents;
        const isModify = modifying_list.includes(data.id);
        if (isModify) {
          table_contents = (<td><input type='text' placeholder={data.name} onChange={this.onModifyValue} /></td>);
          // return (
          //   <tr key={key}>
          //     <th scope="row">{data.id}</th>
          //     <td>
          //       <RemoveBtn id={data.id} onRemoveToggle={onRemove} />
          //       &nbsp;&nbsp;&nbsp;
          //     <ModifyBtn data={data} onModifyToggle={onModify} />
          //     </td>
          //   </tr>
          // );
        } else {
          table_contents = (<td>{data.name}</td>);
          // return (
          //   <tr key={key}>
          //     <th scope="row">{data.id}</th>
          //     <td>{data.name}</td>
          //     <td>
          //       <RemoveBtn id={data.id} onRemoveToggle={onRemove} />
          //       &nbsp;&nbsp;&nbsp;
          //     <ModifyBtn data={data} onModifyToggle={onModify} />
          //     </td>
          //   </tr>
          // );
        }
        return (
          <tr key={key}>
            <th scope="row">{data.id}</th>
            {table_contents}
            {buttons}
          </tr>
        )
      })
    } else {
      contents = null;
    }

    let addForm = null;
    if (addFlag) {
      addForm = (
        <tr>
          <th scope="row">NEW</th>
          <td><input type='text' placeholder='시인 이름을 입력하세요' onChange={onEnterRecord} /></td>
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