import BasicLayout from "../../common/layout/BasicLayout";
import MemberModifyComponent from "../component/MemberModifyComponent";
import PasswordConfirmation from "../component/PasswordConfirmation";
import { useState } from "react";
import MemberSettingsComponent from "../component/MemberSettingsComponent";
import "../styles/MemberCommon.scss"

const MemberModifyPage = () => {
	const [isPasswordVerified, setIsPasswordVerified] = useState(false);

	const handlePasswordSuccess = () => {
		setIsPasswordVerified(true);
	};

	return (
		<BasicLayout>
			<div className="page-container">
				<MemberSettingsComponent />

				{/* 콘텐츠 영역 */}
				<div className="content">
					{!isPasswordVerified ? (
						<PasswordConfirmation onSuccess={handlePasswordSuccess} />
					) : (
						<MemberModifyComponent />
					)}
				</div>
			</div>
		</BasicLayout>
	);
};
export default MemberModifyPage;