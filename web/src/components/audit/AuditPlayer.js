import React, { Component } from 'react';
import ReactPlayer from 'react-player'
import { Slider, Button, Intent } from '@blueprintjs/core';

class AuditPlayer extends Component {
  constructor(props){
    super(props);
    this.state={
      volume: 0.5,
    }
  }
  changeVolume = (volume) => {
    this.setState({
      volume,
    })
  }
  render() {
    const { recording_file, play } = this.props;
    const { volume } = this.state;
    return(
      <div style={{display: 'inline-block', 'vertical-align': 'top', width: '400px'}}>
      <ReactPlayer
        url={recording_file.path} 
        playing={play}
        controls
        volume={volume}
      />
      {play?<div>
      <h3>볼륨</h3>
      <Slider value={volume} onChange={this.changeVolume} max={1} stepSize={0.1}/>
      <Button text='허가' large intent={Intent.SUCCESS} onClick={() => this.props.onClickAccept(recording_file.id)}/>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <Button text='거절' large intent={Intent.DANGER} onClick={() => this.props.onClickReject(recording_file.id)}/>
      </div>
      : null
      }
      </div>
    );
  }
}

export default AuditPlayer;