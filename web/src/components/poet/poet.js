import React from 'react';
import { Table } from 'reactstrap';

export const Poet = ({ data }) => {
  let contents;
  if (data) {
    contents = data.data.data.getAllPoet.map(data => {
      return (
        <tr>
          <th scope="row">{data.id}</th>
          <td>{data.name}</td>
        </tr>
      );
    })
  } else {
    contents = null;
  }
  return (
    <div>
      <Table>
        <thead>
          <tr>
            <th>id</th>
            <th>이름</th>
          </tr>
        </thead>
        <tbody>
          {contents}
        </tbody>
      </Table>
    </div>
  );
}