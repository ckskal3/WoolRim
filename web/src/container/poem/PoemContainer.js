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
      toCreateDataList: [],
      toDeleteDataList: [],
      toUpdateDataList: [],
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
  onDBApply = async () => {
    const { toDeleteDataList, toCreateDataList, toUpdateDataList, data } = this.state;
    if (toCreateDataList.length === 0 &&
      toDeleteDataList.length === 0 &&
      toUpdateDataList.length === 0) {
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
    if (toCreateDataList.length > 0) {
      toCreateDataList.map(v => {
        if (v.key) {
          delete v.key;
        }
      })
      await createPoem(toCreateDataList);
    }
    if (toDeleteDataList.length > 0) {
      await deletePoem(toDeleteDataList);
    }
    if (toUpdateDataList.length > 0) {
      const filteredData = data.filter(v => {
        return toUpdateDataList.includes(v.id);
      })
      await updatePoem(filteredData.map(v => {
        return {
          id: v.id,
          name: v.name,
          content: v.content,
          point: v.point,
          length: v.length,
        };
      }));
    }
    await this.getData();
    this.setState({
      toCreateDataList: [],
      toDeleteDataList: [],
      toUpdateDataList: [],
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
        poet_id: input.poet_id,
        content: input.content,
        point: input.point,
        length: input.length,
        key: date,
      }),
      data: data.concat({ id: 'NEW', ...input, auth_count: 0, key: date }),
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
              name: input.name,
              poet_id: input.poet_id,
              content: input.content,
              point: input.point,
              length: input.length,
              key: input.key,
            };
          }
          return v;
        }),
        data: data.map(v => {
          if (v.key === input.key) {
            return input;
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

  render() {
    const { data, toDeleteDataList, toCreateDataList, toUpdateDataList } = this.state;
    return (
      <div className='main'>
        <ControlBtns title='시' match={this.props.match} onApply={this.onDBApply} />
        <hr />
        <Route path='/poem/create' render={() => <PoemRegister onRegister={this.onCreate} />} />
        <Route path='/poem' render={() =>
          <PoemTable data={data}
            toDeleteDataList={toDeleteDataList}
            onDelete={this.onDelete}
            onUpdate={this.onUpdate}
          />}
        />
      </div>
    );
  }
}