import React from 'react'
import './header.css';
import { NavLink } from 'react-router-dom';
const headerTitleContent = [
  {
    key: 1,
    title: '사용자 관리',
    url: 'user'
  },
  {
    key: 2,
    title: '시인 관리',
    url: 'poet',
  },
  {
    key: 3,
    title: '시 관리',
    url: 'poem',
  },
  {
    key: 4,
    title: '녹음파일 관리',
    url: 'recording',
  },
  {
    key: 5,
    title: '공지사항 관리',
    url: 'notice',
  }
]
const Header = () => {
  return (
    <div className='header'>
      <ul>
        {headerTitleContent.map((item) => {
          return <NavLink key= {item.key} to={'/' + item.url} className="menu-item" activeClassName="active">{item.title}</NavLink>
        })}
      </ul>
    </div>
  )
}

export default Header