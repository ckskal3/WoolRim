import React from 'react';
import { Table } from 'reactstrap';

export const User = ({ data }) => {
  let contents;
  if (data) {
    contents = data.data.data.getAllUser.map(data => {
      return (
        <tr>
          <th scope="row">{data.id}</th>
          <td>{data.name}</td>
          <td>{data.stu_id}</td>
          <td>{data.gender}</td>
          <td>{data.bongsa_time}</td>
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
            <th>학번</th>
            <th>성별</th>
            <th>누적 봉사 시간</th>
          </tr>
        </thead>
        <tbody>
          {contents}
        </tbody>
      </Table>
    </div>
  );
}