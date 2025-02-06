import BasicLayout from "../../common/layout/BasicLayout";
import ChangePasswordComponent from "../component/ChangePasswordComponent";
import PasswordConfirmation from "../component/PasswordConfirmation";
import { useState } from "react";
import MemberSettingsComponent from "../component/MemberSettingsComponent";


const ChangePasswordPage = () => {
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
						<ChangePasswordComponent />
					)}
				</div>
			</div>
		</BasicLayout>
	);
};

export default ChangePasswordPage;