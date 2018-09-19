import React from 'react'
import './header.css';
import { NavLink } from 'react-router-dom';
const headerTitleContent = [
  {
    title: '사용자 관리',
    url: 'user'
  },
  {
    title: '시인 관리',
    url: 'poet',
  },
  {
    title: '시 관리',
    url: 'poem',
  },
  {
    title: '녹음파일 관리',
    url: 'recording',
  },
  {
    title: '공지사항 관리',
    url: 'notice',
  }
]
const Header = () => {
  return (
    <div className='header'>
      <ul>
        {headerTitleContent.map((item) => {
          return <NavLink to={'/' + item.url} className="menu-item" activeClassName="active">{item.title}</NavLink>
        })}
      </ul>
    </div>
  )
}

export default Header