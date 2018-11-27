import React, { Component } from 'react';
import AuditList from '../../components/audit/AuditList';
import AuditPlyer from '../../components/audit/AuditPlayer';
import { getAllRecordingForAudit, acceptRecording, rejectRecording } from './AuditQueries';

export class AuditContainer extends Component {
  constructor(props){
    super(props);
    this.state = {
      play: false,
      selected_user: '',
      selected_recording: '',
      list: [],
    }
  }

  componentDidMount(){
    this.getData()
  }

  getData = async () => {
    const result = await getAllRecordingForAudit();
    this.setState({
      list: result,
    })
  }

  selectRecord = (selected_user, selected_recording) => {
    this.setState({
      selected_user,
      selected_recording,
      play: true,
    })
  }

  accept = async (recording_id) => {
    if(await acceptRecording(recording_id)){
      window.alert('승낙 완료');
    }
    window.location.reload();
  }

  reject = async (recording_id) => {
    if(await rejectRecording(recording_id)){
      window.alert('거절 완료');
    }
    window.location.reload();
  }
  render() {
    const { selected_user, selected_recording, play, list } = this.state;
    return (
      <div>
        <AuditList list={list} onSelectRecord={this.selectRecord}/>
        <AuditPlyer stu_id={selected_user}
         recording_file={selected_recording}
          play={play}
          onClickAccept={this.accept}
          onClickReject={this.reject}/>
      </div>
    );
  }
}