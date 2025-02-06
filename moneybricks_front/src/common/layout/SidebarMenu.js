import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import "../style/SidebarMenu.scss"

const SidebarMenu = ({ menuItems }) => {
	const [openMenu, setOpenMenu] = useState(null); // 펼쳐진 메뉴 상태 관리

	// 메뉴 클릭 시 아코디언 기능 (열기/닫기)
	const handleMenuClick = (index) => {
		setOpenMenu(openMenu === index ? null : index); // 이미 열려있는 메뉴는 닫기
	};

	return (
		<div className="sidebar-menu">
			{menuItems.map((item, index) => (
				<div key={item.label}>
					<h3 onClick={() => handleMenuClick(index)}>{item.label}</h3>
					<ul className={`submenu ${openMenu === index ? 'open' : ''}`}>
						{item.subItems.map((subItem) => (
							<li key={subItem.label}>
								<Link to={subItem.link}>{subItem.label}</Link>
							</li>
						))}
					</ul>
				</div>
			))}
		</div>
	);
};

export default SidebarMenu;
