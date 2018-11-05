import React, { Component } from 'react';
import { MenuItem, EditableText, Button, FormGroup, InputGroup, Intent, NumericInput, Slider } from "@blueprintjs/core";
import { Suggest } from "@blueprintjs/select";
import { getAllPoet } from '../../container/poet/PoetQueries';

class PoemRegister extends Component {
  constructor(props) {
    super(props);
    this.state = {
      poet_list: [],
      poet_id: '',
      poet_name: '',
      poem_name: '',
      content: '',
      point: 0,
      length: 0,
    }
  }

  componentDidMount() {
    this.getPoetData()
  }

  getPoetData = async () => {
    const result = await getAllPoet();
    this.setState({
      poet_list: result,
    })
  }

  onSubmit = () => {
    const { poet_id, poet_name, poem_name, content, point, length } = this.state;
    this.props.onRegister({ name: poem_name, poet: { name: poet_name }, content, point, length, poet_id });
    this.setState({
      poet_id: '',
      poet_name: '',
      poem_name: '',
      content: '',
      point: 0,
      length: 0,
    })
  }

  onNameChange = (event) => {
    this.setState({
      poem_name: event.target.value,
    });
  }

  onPoetChange = (event) => {
    this.setState({
      poet: event.target.value,
    });
  }

  onContentChange = (value) => {
    this.setState({
      content: value,
    });
  }

  onPointChange = (point) => {
    this.setState({
      point,
    });
  }

  onLengthChange = (length) => {
    this.setState({
      length,
    });
  }

  labelRenderer = (val) => {
    return `${val} 분`;
  }

  suggestValueRenderer = (item) => {
    return item.name;
  }

  suggestListRenderer = (item, { handleClick }) => {
    const content = `${item.id}. ${item.name}`;
    return <MenuItem text={content} onClick={handleClick} />
  }

  onPoetSelected = (poet) => {
    this.setState({
      poet_id: poet.id,
      poet_name: poet.name,
    })
  }

  render() {
    return (
      <div>
        <FormGroup inline='true' label='시 제목'>
          <InputGroup
            value={this.state.poem_name}
            onChange={this.onNameChange} />
        </FormGroup>
        <FormGroup inline='true' label='시인'>
          <Suggest
            items={this.state.poet_list}
            itemRenderer={this.suggestListRenderer}
            noResults={<MenuItem disabled={true} text='등록된 시인이 없습니다.' />}
            onItemSelect={this.onPoetSelected}
            inputValueRenderer={this.suggestValueRenderer}
          />
        </FormGroup>
        <FormGroup label='시 내용'>
          <EditableText
            value={this.state.content}
            onChange={this.onContentChange}
            multiline={true}
            placeholder='시 내용을 입력하세요'
            intent={Intent.SUCCESS}
          />
        </FormGroup>
        <FormGroup inline='true' label='부여될 봉사 점수'>
          <NumericInput
            min={0}
            selectAllOnFocus='true'
            value={this.state.point}
            onValueChange={this.onPointChange} />
        </FormGroup>
        <FormGroup label='시 길이 (분)'>
          <Slider
            min={0}
            max={30}
            stepSize={0.1}
            labelStepSize={5}
            labelRenderer={this.labelRenderer}
            onChange={this.onLengthChange}
            value={this.state.length}
          />
        </FormGroup>
        <Button text='등록하기'
          intent={Intent.PRIMARY}
          onClick={this.onSubmit} />
        <hr />
      </div>
    )
  }
}

export default PoemRegister