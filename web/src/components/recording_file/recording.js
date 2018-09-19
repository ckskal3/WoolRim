import React from 'react';
import { Table } from 'reactstrap';

export const Recording = ({ data }) => {
  let contents;
  if (data) {
    contents = data.data.data.getAllRecording.map(data => {
      return (
        <tr>
          <th scope="row">{data.id}</th>
          <td>{data.path}</td>
          <td>{data.auth_flag}</td>
          <td>{data.duration}</td>
          <td>{data.user_id}</td>
          <td>{data.poem_id}</td>
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
            <th>파일 위치</th>
            <th>허가 여부</th>
            <th>재생 시간</th>
            <th>녹음한 사람</th>
            <th>시 제목</th>
          </tr>
        </thead>
        <tbody>
          {contents}
        </tbody>
      </Table>
    </div>
  );
}