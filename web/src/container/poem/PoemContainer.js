import React, { Component } from 'react';
import { PoemList } from '../../components';
import RegistBtn from '../../common/RegistBtn';
import '../Container.css';
import ApplyBtn from '../../common/ApplyBtn';
import { getAllPoem, deletePoem, createPoem, updatePoem } from './PoemQueries';

export class PoemContainer extends Component {
  constructor(props) {
    super(props)
    this.state = {
      onRegitActive: false,
      toDeleteList: [],
      toCreateList: [],
      toModifyList: [],
      poemList: [],
      newPoemRecord: null,
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
      newPoemRecord: { name: e.target.value },
    })
  }

  onRegitToggle = (flag) => {
    if (!flag) {
      const record = this.state.newPoemRecord;
      if (!record) {
        this.onCancelClicked()
        return;
      }
      const { toCreateList, poemList } = this.state;
      this.setState({
        poemList: poemList.concat({ id: 'NEW', name: record.name }),
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
    const { toModifyList, poemList } = this.state;
    const remove_duple = toModifyList.filter(item => {
      return item.id !== input.id;
    })
    poemList.forEach(item => {
      if (item.id === input.id) {
        item.name = input.name;
      }
    })
    this.setState({
      toModifyList: remove_duple.concat(input),
      poemList,
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

    let msg = `${toCreateList.length} 개의 시 생성\n${toDeleteList.length} 개의 시 삭제\n${toModifyList.length} 개의 시 수정 됩니다.
    계속 하시겠습니까?`;

    const isExecute = window.confirm(msg);

    if (!isExecute) {
      return;
    }

    this.createPoem(toCreateList);
    this.deletePoem(toDeleteList);
    this.modifyPoem(toModifyList);
    this.getData();
  }

  createPoem = async (input_list) => {
    const result = await createPoem(input_list);
    if (result.isSuccess) {
      this.setState({
        toCreateList: [],
      })
    } else {
      window.alert('생성 중 오류가 발생하였습니다.');
    }
  }

  deletePoem = async (id_list) => {
    const result = await deletePoem(id_list);
    if (result.isSuccess) {
      this.setState({
        toDeleteList: [],
      })
    } else {
      window.alert('삭제 중 오류가 발생하였습니다.');
    }
  }

  modifyPoem = async (input_list) => {
    const result = await updatePoem(input_list);
    if (result.isSuccess) {
      this.setState({
        toCreateList: [],
      })
    } else {
      window.alert('수정 중 오류가 발생하였습니다.');
    }
  }

  getData = async () => {
    const data = await getAllPoem();
    this.setState({
      poemList: data,
    })
  }

  render() {
    const { poemList, onRegitActive } = this.state;
    return (
      <div className='main'>
        <h2>----시 목록----</h2>
      <PoemList data={poemList} regitFlag={onRegitActive} onRemove={this.onDeleteToggle} onModify={this.onModifyToggle} onEnterRecord={this.onEnterRecord} />
      <RegistBtn root='poem' onRegitToggle={this.onRegitToggle} onRegitCanceled={this.onCancelClicked} />
        <hr/>
      <ApplyBtn onConfirm={this.onApply} isDisable={false} />
      </div>
    );
  }
}