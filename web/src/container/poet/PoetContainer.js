import React, { Component } from 'react';
import { PoetList } from '../../components';
import RegistBtn from '../../common/RegistBtn';
import '../Container.css';
import './PoetContainer.css';
import ApplyBtn from '../../common/ApplyBtn';
import { getAllPoet, deletePoet, createPoet } from './PoetQueries';

export class PoetContainer extends Component {
  constructor(props) {
    super(props)
    this.state = {
      onRegitActive: false,
      toRemoveList: [],
      toCreateList: [],
      toUpdataList: [],
      toModifyList: [],
      poetList: [],
      newPoetRecord: null,
    }
  }

  componentDidMount() {
    this.getData()
  }

  onRegitToggle = (flag) => {
    if (!flag) {
      const record = this.state.newPoetRecord;
      const { toCreateList, poetList } = this.state;
      this.setState({
        poetList: poetList.concat({id:'NEW',name:record.name}),
        toCreateList: toCreateList.concat(record),
      })
    }
    this.setState({
      onRegitActive: flag,
    })
  }

  onCancelClicked = () => {
    this.setState({
      onRegitActive: false,
    })
  }

  onRemoveToggle = (id) => {
    const { toRemoveList } = this.state;
    if (toRemoveList.includes(id)) {
      this.setState({
        toRemoveList: toRemoveList.filter(val => {
          return val !== id;
        }),
      });
    }
    else {
      this.setState({
        toRemoveList: toRemoveList.concat(id),
      });
    }
  }

  onModifyToggle = (input) => {
    console.log(input);
    const { toModifyList, poetList } = this.state;
    const remove_duple = toModifyList.filter(item => {
      return item.id!==input.id;
    })
    poetList.forEach(item => {
      if(item.id === input.id){
        item.name = input.name;
      }
    })
    this.setState({
        toModifyList: remove_duple.concat(input),
        poetList,
    });
  }

  onEnterRecord = (e) => {
    this.setState({
      newPoetRecord: { name: e.target.value },
    })
  }
  onConfirm = () => {
    const { toRemoveList, toCreateList } = this.state;
    if (toRemoveList.length === 0 && toCreateList.length === 0) {
      window.alert('적용할 내용이 없습니다.')
      return;
    }
    let msg = '';
    if (toRemoveList.length > 0) {
      msg = msg.concat(`${toRemoveList}번 레코드가 삭제\n`);
    }
    if (toCreateList.length > 0) {
      msg = msg.concat(`${toCreateList.length}개의 시인이 생성`)
    }
    msg = msg.concat('됩니다. \n계속 하시겠습니까?')
    const isExecute = window.confirm(msg);
    if (!isExecute) {
      return;
    }
    this.createPoet(toCreateList);
    this.deletePoet(toRemoveList);
    this.getData();
  }

  createPoet = async (input_list) => {
    if(input_list.length === 0) return;
    const result = await createPoet(input_list);
    if (result.isSuccess) {
      this.setState({
        toCreateList: [],
      })
      window.alert('생성 되었습니다.');
    } else {
      window.alert('생성 중 오류가 발생하였습니다.');
    }
  }

  deletePoet = async (id_list) => {
    if(id_list.length === 0) return;
    const result = await deletePoet(id_list);
    if (result.isSuccess) {
      this.setState({
        toRemoveList: [],
      })
      window.alert('삭제 되었습니다.');
    } else {
      window.alert('삭제 중 오류가 발생하였습니다.');
    }
  }

  getData = async () => {
    const data = await getAllPoet();
    this.setState({
      poetList: data,
    })
  }

  render() {
    const { poetList, toRemoveList } = this.state;
    return (
      <div className='main'>
        <h2>----시인 목록----</h2>
        <PoetList data={poetList} addFlag={this.state.onRegitActive} onRemove={this.onRemoveToggle} onModify={this.onModifyToggle} removeList={toRemoveList} onEnterRecord={this.onEnterRecord} />
        <RegistBtn root='poet' onRegitToggle={this.onRegitToggle} onRegitCanceled={this.onCancelClicked} />
        <h3>삭제 목록 = [ {toRemoveList.map(item => {
          return item + '번 ';
        })}]</h3>
        <ApplyBtn onConfirm={this.onConfirm} isDisable={false} />
      </div>
    );
  }
}