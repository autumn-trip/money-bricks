import BasicLayout from "../../common/layout/BasicLayout";
import PasswordConfirmation from "../component/PasswordConfirmation";
import { useState } from "react";
import MemberDeleteComponent from "../component/MemberDeleteComponent";
import MemberSettingsComponent from "../component/MemberSettingsComponent";

const MemberDeletePage = () => {
	const [isPasswordVerified, setIsPasswordVerified] = useState(false); // 비밀번호 확인 여부

	const handlePasswordSuccess = () => {
		setIsPasswordVerified(true); // 비밀번호 확인 성공 시 상태 변경
	};

	return (
		<BasicLayout>
			<div className="page-container">
				<MemberSettingsComponent />

				{/* 콘텐츠 영역 */}
				<div className="content">
					{!isPasswordVerified ? (
						<PasswordConfirmation onSuccess={handlePasswordSuccess} /> // 비밀번호 확인
					) : (
						<MemberDeleteComponent /> // 회원 탈퇴 컴포넌트
					)}
				</div>
			</div>
		</BasicLayout>
	);
};

export default MemberDeletePage;
