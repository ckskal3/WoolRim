import React, { Component } from 'react'
import { Column, Table, Cell, EditableCell } from "@blueprintjs/table";
import { Intent } from '@blueprintjs/core';

import { dataKey, dateFormatter } from '../../common/Tools';
import Remover from '../../common/Remover';

export class RecordingTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
    }
  };

  onClickCellToDelete = (data) => {
    this.props.onDelete(data)
  }

  cellRenderer = (rowIndex, columnIndex) => {
    if (columnIndex === 7) {
      columnIndex--;
    }
    const { data, toDeleteDataList } = this.props;
    const columnName = dataKey(data, columnIndex);
    if (columnName === 'date') {
      data[rowIndex][columnName] = dateFormatter(data[rowIndex][columnName]);
    }
    if (toDeleteDataList.includes(data[rowIndex].id)) {
      return <Cell key={data[rowIndex].id} intent={Intent.DANGER}><div onClick={this.onClickCellToDelete}>{data[rowIndex][columnName]}</div></Cell>
    } else {
      return <Cell key={data[rowIndex].id}>{data[rowIndex][columnName]}</Cell>
    }
  }

  joinedCellRenderer = (rowIndex, columnIndex) => {
    const { data, toDeleteDataList } = this.props;
    const columnName = dataKey(data, columnIndex);
    if (toDeleteDataList.includes(data[rowIndex].id)) {
      return (
        <Cell
          key={data[rowIndex].id}
          intent={Intent.DANGER}>
          {data[rowIndex][columnName].name}
        </Cell>
      );
    } else {
      return (
        <Cell
          key={data[rowIndex].id}>
          {data[rowIndex][columnName].name}
        </Cell>
      );
    }
  }

  joinedJoinedCellRenderer = (rowIndex, columnIndex) => {
    const { data, toDeleteDataList } = this.props;
    const columnName = dataKey(data, columnIndex);
    if (toDeleteDataList.includes(data[rowIndex].id)) {
      return (
        <Cell
          key={data[rowIndex].id}
          intent={Intent.DANGER}>
          {data[rowIndex].poem.poet.name}
        </Cell>
      );
    } else {
      return (
        <Cell
          key={data[rowIndex].id}>
          {data[rowIndex].poem.poet.name}
        </Cell>
      );
    }
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
  render() {
    const { data } = this.props
    return (
      <div>
        <Table numRows={data.length}
          enableGhostCells='true'
          enableRowHeader='false'>
          <Column name='id' cellRenderer={this.cellRenderer} />
          <Column name='파일정보' cellRenderer={this.cellRenderer} />
          <Column name='생성날짜' cellRenderer={this.cellRenderer} />
          <Column name='허가여부' cellRenderer={this.cellRenderer} />
          <Column name='작성자' cellRenderer={this.joinedCellRenderer} />
          <Column name='시제목' cellRenderer={this.joinedCellRenderer} />
          <Column name='시인' cellRenderer={this.joinedJoinedCellRenderer} />
          <Column name='녹음길이' cellRenderer={this.cellRenderer} />
          <Column name='관리' cellRenderer={this.managementCellRenderer} />
        </Table>
      </div>
    )
  }
}