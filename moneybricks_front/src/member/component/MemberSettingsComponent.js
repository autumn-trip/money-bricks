import React from 'react';

import SidebarMenu from "../../common/layout/SidebarMenu";

const MemberSettingsComponent = () => {

	// 메뉴 항목 정의
	const menuItems = [
		{
			label: '설정',  // 메뉴 제목
			subItems: [
				{
					label: '회원정보 수정',
					link: '/member/settings/edit-member',
				},
				{
					label: '비밀번호 변경',
					link: '/member/settings/change-password',
				},
				{
					label: '회원 탈퇴',
					link: '/member/settings/delete-account',
				},
			],
		},
	];

	return (
		<div className="member-settings-sidebar">
			<SidebarMenu menuItems={menuItems} />
		</div>
	);
};

export default MemberSettingsComponent;
