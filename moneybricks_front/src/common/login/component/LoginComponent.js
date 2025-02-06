import React, { useState } from "react";
import useCustomLogin from "../../hooks/useCustomLogin";
import "../styles/LoginComponent.scss";
import LoadingSpinner from "../../component/LoadingSpinner";
import { Link } from "react-router-dom";

const initState = {
	username: "", password: "",
};

// logo 이미지
const logoSrc =
	`${process.env.PUBLIC_URL}/images/moneybricks_logo.png`;

const LoginComponent = () => {
	const [loginParam, setLoginParam] = useState({ ...initState });

	const [errorMessage, setErrorMessage] = useState(""); // 에러 메시지 상태 추가
	const [isLoading, setIsLoading] = useState(false); // 로딩 상태 추가

	const { doLogin, moveToPath } = useCustomLogin();

	const handleChange = (e) => {
		loginParam[e.target.name] = e.target.value;
		setLoginParam({ ...loginParam });
	};

	const handleClickLogin = (e) => {
		e.preventDefault();
		setErrorMessage(""); // 에러 메시지 초기화
		setIsLoading(true); // 로그인 시 로딩 시작

		doLogin(loginParam)
			.then((data) => {
				setIsLoading(false); // 로그인 완료 후 로딩 종료
				console.log(data);
				// 데이터가 없거나 응답이 유효하지 않을 때 처리
				if (!data || typeof data !== "object") {
					setErrorMessage("아이디와 비밀번호를 입력해 주세요.");
					return;
				}

				// 서버에서 에러 메시지 전달
				if (data.error) {
					setErrorMessage(data.message || "아이디와 비밀번호를 다시 확인하세요.");
					return;
				}

				moveToPath("/"); // 로그인 성공 시 홈으로 이동
			})
			.catch((error) => {
				setIsLoading(false); // 에러 발생 시 로딩 종료
				// 네트워크 오류 또는 기타 예외 처리
				setErrorMessage(error.message || "서버와 통신 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
			});
	};

	return (<div className="login-container">
		<div className="login-box">
			<Link to="/" className="logo-link">
				<img src={logoSrc} alt="Logo" />
			</Link>

			{/* 로딩 화면을 LoadingSpinner로 대체 */}
			<LoadingSpinner isLoading={isLoading} />

			<form onSubmit={handleClickLogin}>
				<div className="login-form-group">
					<label htmlFor="username"></label>
					<input
						id="username"
						name="username"
						type="text"
						placeholder="아이디를 입력해 주세요"
						value={loginParam.username}
						onChange={handleChange}
					/>
				</div>
				<div className="login-form-group">
					<label htmlFor="password"></label>
					<input
						id="password"
						name="password"
						type="password"
						placeholder="비밀번호를 입력해 주세요"
						value={loginParam.password}
						onChange={handleChange}
					/>
				</div>
				{/* 에러 메시지 표시 */}
				{errorMessage && <div className="error-message">{errorMessage}</div>}
				<button type="submit" className="login-form-button" disabled={isLoading}>
					{isLoading ? "로그인 중..." : "로그인하기"}
				</button>
			</form>
			<div className="member-register-link">
				<span>아직 MONEYBRICKS의 회원이 아니신가요? </span>
				<Link to="/member/sign-up">회원가입하기</Link>
			</div>
		</div>
	</div>);
};

export default LoginComponent;
