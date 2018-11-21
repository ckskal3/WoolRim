import React, { Component } from 'react'
import { Column, Table, Cell, EditableCell } from "@blueprintjs/table";
import { Intent, EditableText, Button } from '@blueprintjs/core';

import { dataKey, dateFormatter } from '../../common/Tools';
import Remover from '../../common/Remover';
import { getAllPoet } from '../../container/poet/PoetQueries';

export class PoemTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
      contents: null,
      modifyisActive: false,
      current_row: null,
    }
  }

  onClickCellToDelete = (data) => {
    this.props.onDelete(data)
  }

  componentDidMount() {
    this.getPoetData()
  }

  getPoetData = async () => {
    const result = await getAllPoet();
    this.setState({
      poet_list: result,
    })
  }
  cellRenderer = (rowIndex, columnIndex) => {
    const { data } = this.props;
    const columnName = dataKey(data, columnIndex);
    if (columnName === 'date') {
      data[rowIndex][columnName] = dateFormatter(data[rowIndex][columnName]);
    }
    return <Cell key={data[rowIndex].id}>{data[rowIndex][columnName]}</Cell>
  }

  editableCellRenderer = (rowIndex, columnIndex) => {
    const { data } = this.props;
    const columnName = dataKey(data, columnIndex);
    return (
      <EditableCell
        key={data[rowIndex].id}
        rowIndex={rowIndex}
        columnIndex={columnIndex}
        value={data[rowIndex][columnName]}
        onConfirm={this.onCellConfirm}
      />
    );
  }

  joinedCellRenderer = (rowIndex, columnIndex) => {
    const { data } = this.props;
    const columnName = dataKey(data, columnIndex);
    return (
      <EditableCell
        key={data[rowIndex].id}
        rowIndex={rowIndex}
        columnIndex={columnIndex}
        value={data[rowIndex][columnName].name}
        onConfirm={this.onJoinCellConfirm} />
    );
  }
  onJoinCellConfirm = (value, rowIndex, columnIndex) => {
    const { poet_list } = this.state;
    const { data, onUpdate } = this.props
    const poet = poet_list.filter((poet) => {
      return poet.name === value;
    })
    const columnName = dataKey(data, columnIndex);
    if (poet.length === 0) {
      alert('없는 시인 입니다!')
      return;
    }
    data[rowIndex][columnName] = value;
    onUpdate(data[rowIndex]);
  }
  managementCellRenderer = (rowIndex, columnIndex) => {
    const { data } = this.props;
    return (
      <Cell
        key={data[rowIndex].id}>
        <Remover data={data[rowIndex]}
          onClick={this.onClickCellToDelete} />
      </Cell>
    )
  }

  poemContentsCellRenderer = (rowIndex, columnIndex) => {
    const { data } = this.props;
    return (
      <Cell
        key={data[rowIndex].id}>
        <Button small={true} minimal={true} onClick={() =>
          this.showContentsModifier(rowIndex, columnIndex)} intent={Intent.SUCCESS}> 보기 </Button>
      </Cell>
    )
  }

  showContentsModifier = (rowIndex, columnIndex) => {
    const { data } = this.props;
    const { modifyisActive } = this.state;
    this.setState({
      contents: data[rowIndex].content,
      modifyisActive: !modifyisActive,
      current_row: rowIndex,
    })
  }

  onCellConfirm = (value, rowIndex, columnIndex) => {
    const { data, onUpdate } = this.props
    const columnName = dataKey(data, columnIndex);
    data[rowIndex][columnName] = value;
    onUpdate(data[rowIndex]);
  }

  render() {
    const { data } = this.props;
    const { modifyisActive, contents, current_row } = this.state;
    const columnWidths = [
      40, // id
      200, // 제목
      70, // 시인
      60, // 내용
      70, // 봉사시간
      90, // 길이
      100, // 인증횟수
      100, // 인증횟수(여)
      100, // 인증횟수(남)
      100, // 관리
    ]
    return (
      <div>
        {modifyisActive ? [
          <EditableText value={contents}
            multiline={true}
            onChange={(value) => {
              this.setState({
                contents: value,
              })
            }} />,
          <Button fill={true} intent={Intent.PRIMARY} onClick={() => {
            this.setState({
              modifyisActive: false,
            })
            this.onCellConfirm(contents, current_row, 3)
          }}>수정하기</Button>]
          : null}
        <Table numRows={data.length}
          enableGhostCells='true'
          enableRowHeader='false'
          columnWidths={columnWidths}>
          <Column name='id' cellRenderer={this.cellRenderer} />
          <Column name='제목' cellRenderer={this.editableCellRenderer} />
          <Column name='시인' cellRenderer={this.joinedCellRenderer} />
          <Column name='내용' cellRenderer={this.poemContentsCellRenderer} />
          <Column name='봉사시간' cellRenderer={this.editableCellRenderer} />
          <Column name='길이 (분)' cellRenderer={this.editableCellRenderer} />
          <Column name='총 인증 횟수' cellRenderer={this.cellRenderer} />
          <Column name='인증 횟수(여)' cellRenderer={this.cellRenderer} />
          <Column name='인증 횟수(남)' cellRenderer={this.cellRenderer} />
          <Column name='관리' cellRenderer={this.managementCellRenderer} />
        </Table>
      </div>
    )
  }
}
