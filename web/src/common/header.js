import React from 'react'
import { NavLink } from 'react-router-dom';
import './header.css'
import { Navbar, NavbarGroup, NavbarHeading, NavbarDivider, Button, Classes } from '@blueprintjs/core';
const headerTitleContent = [
  {
    key: 1,
    title: '사용자 관리',
    url: 'user',
    icon: 'people',
  },
  {
    key: 2,
    title: '시인 관리',
    url: 'poet',
    icon: 'mugshot',
  },
  {
    key: 3,
    title: '시 목록 관리',
    url: 'poem',
    icon: 'properties',
  },
  {
    key: 4,
    title: '녹음파일 관리',
    url: 'recording',
    icon: 'inbox',
  },
  {
    key: 5,
    title: '공지사항 관리',
    url: 'notice',
    icon: 'volume-off'
  }
]
const Header = () => {
  return (
    <Navbar>
      <NavbarGroup>
        <NavbarHeading>울림 관리자</NavbarHeading>
        <NavbarDivider />
        {headerTitleContent.map((item) => {
          return (
            <NavLink key={item.key} to={'/' + item.url} className='nav-link'>
              <Button className={Classes.MINIMAL} icon={item.icon} text={item.title} />
            </NavLink>
          );
        })}
        </NavbarGroup>
    </Navbar>
  )
}

export default Header