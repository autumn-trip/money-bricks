import React from "react";
import useCustomLogin from "../../common/hooks/useCustomLogin";
import BasicLayout from "../../common/layout/BasicLayout";

const MemberSignupSuccessPage = () => {
	const {moveToLogin} = useCustomLogin()
	const handleLogin = () => {
		moveToLogin();
	};

	return (
		<div className="signup-success-page">
			<BasicLayout/>
			<h1>회원가입을 축하합니다! 🎉</h1>
			<p>계정을 성공적으로 생성했습니다. 지금 로그인해서 서비스를 이용해보세요!</p>
			<button onClick={handleLogin} className="signup-success-login-btn">
				로그인하기
			</button>
		</div>
	);
};

export default MemberSignupSuccessPage;
