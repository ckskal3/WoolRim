import React, { Component } from 'react'

class ModifyPoet extends Component {
  constructor(props) {
    super(props);
    const { poet, onApply } = this.props;
    this.state = { 
      poet,
      onApply,
    } 
  }

  onApply = () => {
    const { poet } = this.state;
    this.state.onApply(poet);
  }

  onNameChanged = (e) => {
    const { poet } = this.state;
    poet.name = e.target.value;
    this.setState({
      poet,
    })
  }
  
  render() {
    const { name } = this.state;
    return (
      <div>
        <ul>
          <li>이름 : <input type='text' placeholder={name}></input></li>
        </ul>
      </div>
    )
  }
}

export default ModifyPoet