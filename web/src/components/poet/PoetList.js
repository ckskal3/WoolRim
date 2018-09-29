import React from 'react';
import { Table } from 'reactstrap';
import './PoetList.css'
import RemoveBtn from '../../common/RemoveBtn';
import ModifyBtn from '../../common/ModifyBtn';

export const PoetList = ({ data, addFlag, onRemove, removeList, onEnterRecord }) => {
  let contents;
  let key = 0;
  if (data) {
    contents = data.map(data => {
      key = data.id === 'NEW'? Number(key)+1 : data.id;
      return (
        <tr key={key}>
          <th scope="row">{data.id}</th>
          <td>{data.name}</td>
          <td>
            {removeList.includes(data.id)?
              <RemoveBtn id={data.id} onRemoveToggle={onRemove} cancelable={true} />:
              <RemoveBtn id={data.id} onRemoveToggle={onRemove} cancelable={false}/>}
              &nbsp;&nbsp;&nbsp;
            <ModifyBtn />
          </td>
        </tr>
      );
    })
  } else {
    contents = null;
  }
  let addForm = null;
  if (addFlag) {
    addForm = (
      <tr>
        <th scope="row">NEW</th>
        <td><input type='text' placeholder='시인 이름을 입력하세요' onChange={onEnterRecord}/></td>
        <td></td>
      </tr>
    )
  }

  return (
    <div>
      <Table className='table'>
        <thead>
          <tr>
            <th>id</th>
            <th>이름</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {contents}
          {addForm}
        </tbody>
      </Table>
    </div>
  );
}