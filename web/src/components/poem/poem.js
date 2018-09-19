import React from 'react';
import { Table } from 'reactstrap';

export const Poem = ({ data }) => {
  let contents;
  if (data) {
    contents = data.data.data.getAllPoem.map(data => {
      return (
        <tr>
          <th scope="row">{data.id}</th>
          <td>{data.name}</td>
          <td>{data.content}</td>
          <td>{data.auth_count}</td>
          <td>{data.point}</td>
          <td>{data.poet_id}</td>
          <td>{data.lenght}</td>
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
            <th>내용</th>
            <th>카운트</th>
            <th>봉사 점수</th>
            <th>시인</th>
            <th>길이</th>
          </tr>
        </thead>
        <tbody>
          {contents}
        </tbody>
      </Table>
    </div>
  );
}