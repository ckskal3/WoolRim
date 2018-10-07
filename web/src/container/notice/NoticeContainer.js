import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import { NoticeTable } from '../../components';
import axios from 'axios';
import '../Container.css'
import data from './NoticeSampleData';
import ControlBtns from '../../common/ControlBtns';
import NoticeRegister from '../../components/notice/NoticeRegister';

export class NoticeContainer extends Component {
  constructor(props) {
    super(props)
    const { match } = props;
    this.state = {
      match,
      data,
    }
  }

  // componentDidMount() {
  //   this.getData()
  // }

  // getData = async () => {
  //   const query = `query {
  //     getAllNotice{
  //       id
  //       content
  //       date
  //       user_id
  //     }
  //   }`;
  //   const result = await axios.post('http://localhost:3000/graphql', { query });
  //   this.setState({
  //     data: result,
  //   })
  // }

  render() {
    const { data, match } = this.state;
    return (
      <div className='main'>
        <ControlBtns title='공지사항' match={match} />
        <hr />
        <Route path='/notice/create' render={() => <NoticeRegister />} />
        <Route path='/notice' render={() => <NoticeTable data={data} />} />
      </div>
    );
  }
}