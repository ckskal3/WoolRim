import React, { Component } from 'react'
import { Column, Table, Cell, EditableCell } from "@blueprintjs/table";

import { dataKey, dateFormatter } from '../../common/Tools';
import Remover from '../../common/Remover';

export class PoetTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
    }
  };

  onClickCellToDelete = (data) => {
    this.props.onDelete(data)
  }

  cellRenderer = (rowIndex, columnIndex) => {
    const { data } = this.props;
    const columnName = dataKey(data, columnIndex);
    if (columnName === 'date') {
      data[rowIndex][columnName] = dateFormatter(data[rowIndex][columnName]);
    }
    return <Cell key={data[rowIndex].id}>{data[rowIndex][columnName]}</Cell>
  }

  onCellConfirm = (value, rowIndex, columnIndex) => {
    const { data, onUpdate } = this.props
    const columnName = dataKey(data, columnIndex);
    data[rowIndex][columnName] = value;
    onUpdate(data[rowIndex]);
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
        onConfirm={this.onCellConfirm}
      />
    );
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
          <Column name='이름' cellRenderer={this.editableCellRenderer} />
          <Column name='관리' cellRenderer={this.managementCellRenderer} />
        </Table>
      </div>
    )
  }
}