import React, { Component } from 'react';
import { PoetList } from '../../components';
import RegistBtn from '../../common/RegistBtn';
import '../Container.css';
import './PoetContainer.css';
import ApplyBtn from '../../common/ApplyBtn';
import { getAllPoet, deletePoet, createPoet, updatePoet } from './PoetQueries';

export class PoetContainer extends Component {
  constructor(props) {
    super(props)
    this.state = {
      onRegitActive: false,
      toDeleteList: [],
      toCreateList: [],
      toModifyList: [],
      poetList: [],
      newPoetRecord: null,
    }
  }

  componentDidMount() {
    this.getData()
  }

  onCancelClicked = () => {
    this.setState({
      onRegitActive: false,
    })
  }

  onEnterRecord = (e) => {
    this.setState({
      newPoetRecord: { name: e.target.value },
    })
  }

  onRegitToggle = (flag) => {
    if (!flag) {
      const record = this.state.newPoetRecord;
      if (!record) {
        this.onCancelClicked()
        return;
      }
      const { toCreateList, poetList } = this.state;
      this.setState({
        poetList: poetList.concat({ id: 'NEW', name: record.name }),
        toCreateList: toCreateList.concat(record),
      })
    }
    this.setState({
      onRegitActive: flag,
    })
  }

  onDeleteToggle = (id) => {
    const { toDeleteList } = this.state;
    if (toDeleteList.includes(id)) {
      this.setState({
        toDeleteList: toDeleteList.filter(val => {
          return val !== id;
        }),
      });
    }
    else {
      this.setState({
        toDeleteList: toDeleteList.concat(id),
      });
    }
  }

  onModifyToggle = (input) => {
    if (!input.name) return;
    const { toModifyList, poetList } = this.state;
    const remove_duple = toModifyList.filter(item => {
      return item.id !== input.id;
    })
    poetList.forEach(item => {
      if (item.id === input.id) {
        item.name = input.name;
      }
    })
    this.setState({
      toModifyList: remove_duple.concat(input),
      poetList,
    });
  }

  onApply = () => {
    const { toDeleteList, toCreateList, toModifyList } = this.state;
    if (toDeleteList.length === 0 &&
      toCreateList.length === 0 &&
      toModifyList.length === 0) {
      window.alert('적용할 내용이 없습니다.')
      return;
    }

    let msg = `${toCreateList.length} 개의 시인 생성\n${toDeleteList.length} 개의 시인 삭제\n${toModifyList.length} 개의 시인 수정 됩니다.
    계속 하시겠습니까?`;

    const isExecute = window.confirm(msg);

    if (!isExecute) {
      return;
    }

    this.createPoet(toCreateList);
    this.deletePoet(toDeleteList);
    this.modifyPoet(toModifyList);
    this.getData();
  }

  createPoet = async (input_list) => {
    const result = await createPoet(input_list);
    if (result.isSuccess) {
      this.setState({
        toCreateList: [],
      })
    } else {
      window.alert('생성 중 오류가 발생하였습니다.');
    }
  }

  deletePoet = async (id_list) => {
    const result = await deletePoet(id_list);
    if (result.isSuccess) {
      this.setState({
        toDeleteList: [],
      })
    } else {
      window.alert('삭제 중 오류가 발생하였습니다.');
    }
  }

  modifyPoet = async (input_list) => {
    const result = await updatePoet(input_list);
    if (result.isSuccess) {
      this.setState({
        toCreateList: [],
      })
    } else {
      window.alert('수정 중 오류가 발생하였습니다.');
    }
  }

  getData = async () => {
    const data = await getAllPoet();
    this.setState({
      poetList: data,
    })
  }

  render() {
    const { poetList, toDeleteList } = this.state;
    return (
      <div className='main'>
        <h2>----시인 목록----</h2>
        <PoetList data={poetList} addFlag={this.state.onRegitActive} onRemove={this.onDeleteToggle} onModify={this.onModifyToggle} removeList={toDeleteList} onEnterRecord={this.onEnterRecord} />
        <RegistBtn root='poet' onRegitToggle={this.onRegitToggle} onRegitCanceled={this.onCancelClicked} />
        <h3>삭제 목록 = [ {toDeleteList.map(item => {
          return item + '번 ';
        })}]</h3>
        <ApplyBtn onConfirm={this.onApply} isDisable={false} />
      </div>
    );
  }
}