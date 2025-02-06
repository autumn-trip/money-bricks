import React, { useState } from "react";
import LoadingSpinner from "../../common/component/LoadingSpinner";
import { useNavigate } from "react-router-dom";
import useCustomLogin from "../../common/hooks/useCustomLogin";
import "../styles/MemberDeleteComponent.scss"
import {deleteMember} from "../api/memberApi";

const MemberDeleteComponent = () => {
	const [isLoading, setIsLoading] = useState(false);
	const { doLogout } = useCustomLogin();
	const navigate = useNavigate();

	// 탈퇴 요청 처리
	const handleDeleteAccount = async () => {

		if (!window.confirm("정말로 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) {
			return;
		}

		setIsLoading(true);

		try {
			await deleteMember(); // 서버에 탈퇴 요청
			alert("회원 탈퇴가 완료되었습니다. 이용해주셔서 감사합니다.");
			doLogout();
			navigate("/");
		} catch (error) {
			console.error("회원 탈퇴 실패:", error);
			alert("회원 탈퇴 중 문제가 발생했습니다. 다시 시도해주세요.");
		} finally {
			setIsLoading(false);
		}
	};

	return (
		<div className="delete-account-container">
			{isLoading && <LoadingSpinner />}
			<div className="delete-account-content">
				<h1>회원 탈퇴</h1>
				<div>
					<p className="warning-message">
						회원 탈퇴 시 모든 데이터가 삭제되며, 복구할 수 없습니다.
					</p>
					<button
						className="delete-account-btn"
						onClick={handleDeleteAccount}
						disabled={isLoading}
					>
						{isLoading ? "탈퇴 처리 중..." : "회원 탈퇴"}
					</button>
				</div>
			</div>
		</div>
	);
};

export default MemberDeleteComponent;
