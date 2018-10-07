import React, { Component } from 'react'
import { Column, Table, Cell, EditableCell } from "@blueprintjs/table";

import { dataKey } from '../../common/Tools';

export class NoticeTable extends Component {
  constructor(props) {
    super(props);
    const datas = props.data || [];
    this.state = {
      datas,
      changedIdList: [],
    }
  };

  cellRenderer = (rowIndex, columnIndex) => {
    const { datas } = this.state;
    const columnName = dataKey(datas, columnIndex);
    return <Cell>{datas[rowIndex][columnName]}</Cell>
  }

  onCellConfirm = (value, rowIndex, columnIndex) => {
    const { datas, changedIdList } = this.state;
    const columnName = dataKey(datas, columnIndex);
    datas[rowIndex][columnName] = value;
    if (!changedIdList.includes(datas[rowIndex].id)) {
      changedIdList.push(datas[rowIndex].id);
    }
    this.setState({
      datas,
      changedIdList,
    })
  }

  editableCellRenderer = (rowIndex, columnIndex) => {
    const { datas } = this.state;
    const columnName = dataKey(datas, columnIndex);
    return (
      <EditableCell
        rowIndex={rowIndex}
        columnIndex={columnIndex}
        value={datas[rowIndex][columnName]}
        onConfirm={this.onCellConfirm}
      />
    );
  }

  render() {
    const { datas } = this.state
    return (
      <div>
        <Table numRows={datas.length}>
          <Column name='id' cellRenderer={this.cellRenderer} />
          <Column name='내용' cellRenderer={this.editableCellRenderer} />
          <Column name='날짜' cellRenderer={this.editableCellRenderer} />
          <Column name='작성자' cellRenderer={this.editableCellRenderer} />
        </Table>
      </div>
    )
  }
}