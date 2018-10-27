import React, { Component } from 'react'
import { Column, Table, Cell } from "@blueprintjs/table";

import { dataKey, dateFormatter } from '../../common/Tools';

export class UserTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
    }
  };

  cellRenderer = (rowIndex, columnIndex) => {
    const { data } = this.props;
    const columnName = dataKey(data, columnIndex);
    if (columnName === 'profile' && !data[rowIndex][columnName]) {
      data[rowIndex][columnName] = '없음'
    }
    if (columnName === 'date') {
      data[rowIndex][columnName] = dateFormatter(data[rowIndex][columnName]);
    }
    return <Cell key={data[rowIndex].id}>{data[rowIndex][columnName]}</Cell>
  }

  render() {
    const { data } = this.props
    return (
      <div>
        <Table numRows={data.length}
          enableGhostCells='true'
          enableRowHeader='false'>
          <Column name='id' cellRenderer={this.cellRenderer} />
          <Column name='이름' cellRenderer={this.cellRenderer} />
          <Column name='대학교' cellRenderer={this.cellRenderer} />
          <Column name='학번' cellRenderer={this.cellRenderer} />
          <Column name='성별' cellRenderer={this.cellRenderer} />
          <Column name='생성날짜' cellRenderer={this.cellRenderer} />
          <Column name='봉사시간' cellRenderer={this.cellRenderer} />
          <Column name='프로필' cellRenderer={this.cellRenderer} />
        </Table>
      </div>
    )
  }
}