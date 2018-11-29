import { Button, PanelStack } from "@blueprintjs/core";
import React, { Component } from 'react';

import './AuditList.css';
class Panel extends Component {
  selectUser = (selected_user) => {
    this.props.openPanel({
      component: Panel,
      props: {
        panelNumber: 2,
        list: this.props.list,
        selected_user,
        onSelectRecord: this.props.onSelectRecord,
      },
      title: '녹음 목록',
    });
  }

  selectFile = (selected_file) => {
    const { selected_user } = this.props;
    this.props.onSelectRecord(selected_user, selected_file);
  }

  render() {
    const { panelNumber, list, selected_user } = this.props;
    if (panelNumber === 1) {
      return (
        <User user_list={list.map(item => item.user)} onClick={this.selectUser} />
      )
    } else {
      return (
        <RecordingFile recording_file_list={list.find(v => v.user.id === selected_user).recording_list} onClick={this.selectFile} />
      )
    }
  }
}

const User = (props) => {
  return (
    <div>
      {props.user_list.map(user =>
        <Button minimal text={`${user.name}(${user.stu_id})`} onClick={() => props.onClick(user.id)} />
      )}
    </div>
  );
}

const RecordingFile = (props) => {
  return (
    <div>
      {props.recording_file_list.map(file =>
        <Button fill minimal text={file.path.replace(/^.+\//, '')} onClick={() => props.onClick(file)} />
      )}
    </div>
  );
}

class AuditList extends Component {
  selectRecord = (selected_user, selected_file) => {
    this.props.onSelectRecord(selected_user, selected_file);
  }
  render() {
    const { list } = this.props;
    if (list.length === 0) {
      return <div>심사할 목록 없음</div>
    } else {
      return (
        <div className='root'>
          <PanelStack
            className='panel'
            initialPanel={{
              component: Panel,
              props: {
                panelNumber: 1,
                list,
                selected_user: '',
                onSelectRecord: this.selectRecord
              },
              title: '유저 목록'
            }}
          />
        </div>
      );
    }
  }
}

export default AuditList;