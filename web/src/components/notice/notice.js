import React from 'react';
import { Table } from 'reactstrap';

export const Notice = ({ data }) => {
  let contents;
  if (data) {
    contents = data.data.data.getAllNotice.map(data => {
      return (
        <tr>
          <th scope="row">{data.id}</th>
          <td>{data.content}</td>
          <td>{data.date}</td>
          <td>{data.user_id}</td>
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
            <th>내용</th>
            <th>날짜</th>
            <th>작성자</th>
          </tr>
        </thead>
        <tbody>
          {contents}
        </tbody>
      </Table>
    </div>
  );
}