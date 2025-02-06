import React, { useState } from "react";
import { changePassword } from "../api/memberApi";
import useCustomLogin from "../../common/hooks/useCustomLogin";
import "../styles/ChangePasswordComponent.scss"

// 유효성 검사 규칙
const validationRule = {
	password: {
		pattern: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/,
		message: "비밀번호는 영문자, 숫자, 특수문자(@$!%*#?&) 조합 8자리 이상 입력해주세요.",
	},
};

const initState = {
	password: "",
	confirmPassword: "",
};

const ChangePasswordComponent = () => {
	const [formData, setFormData] = useState(initState);
	const [errors, setErrors] = useState({});
	const [isLoading, setIsLoading] = useState(false);

	const { doLogout, moveToLogin } = useCustomLogin();

	// 실시간 유효성 검사
	const validateField = (name, value) => {
		if (!value) return "필수 입력 항목입니다";

		if (name === "password") {
			if (!validationRule.password.pattern.test(value)) {
				return validationRule.password.message;
			}
		}

		if (name === "confirmPassword") {
			if (value !== formData.password) {
				return "비밀번호가 일치하지 않습니다";
			}
		}

		return "";
	};

	// 전체 폼 유효성 검사
	const validateForm = () => {
		const newErrors = {};
		Object.keys(formData).forEach((field) => {
			const error = validateField(field, formData[field]);
			if (error) newErrors[field] = error;
		});

		setErrors(newErrors);
		return Object.keys(newErrors).length === 0; // 에러가 없으면 true 반환
	};

	// 입력값 변경 핸들러
	const handleChange = (e) => {
		const { name, value } = e.target;
		setFormData(prev => ({ ...prev, [name]: value }));

		// 실시간 유효성 검사
		const error = validateField(name, value);
		setErrors(prev => ({ ...prev, [name]: error }));
	};

	// 폼 제출 핸들러
	const handleSubmit = async (e) => {
		e.preventDefault();

		// 전체 폼 유효성 검사
		if (!validateForm()) {
			return;
		}

		setIsLoading(true);

		try {
			await changePassword(formData);
			alert("비밀번호가 성공적으로 변경되었습니다.");

			// 비밀번호 변경 후 로그아웃 및 로그인 페이지로 리디렉션
			doLogout();
			moveToLogin();
		} catch (error) {
			console.error("비밀번호 변경 요청 실패:", error);
			if (error.response?.data) {
				alert(error.response.data);
			} else {
				alert("비밀번호 변경에 실패했습니다.");
			}
		} finally {
			setIsLoading(false);
		}
	};

	return (
		<div className="change-password-container">
			<form onSubmit={handleSubmit} className="change-password-form">
				<h1>비밀번호 변경</h1>
				<div className="form-group">
					<label htmlFor="password" className="form-label">
						새 비밀번호
					</label>
					<input
						type="password"
						id="password"
						name="password"
						value={formData.password}
						onChange={handleChange}
						className="form-input"
						placeholder="새 비밀번호를 입력하세요"
					/>
					{errors.password && (
						<div className="error-message">{errors.password}</div>
					)}
				</div>

				<div className="form-group">
					<label htmlFor="confirmPassword" className="form-label">
						새 비밀번호 확인
					</label>
					<input
						type="password"
						id="confirmPassword"
						name="confirmPassword"
						value={formData.confirmPassword}
						onChange={handleChange}
						className="form-input"
						placeholder="새 비밀번호를 다시 입력하세요"
					/>
					{errors.confirmPassword && (
						<div className="error-message">{errors.confirmPassword}</div>
					)}
				</div>

				<button
					type="submit"
					className="change-password-btn"
					disabled={isLoading}
				>
					{isLoading ? "변경 중..." : "변경하기"}
				</button>
			</form>
		</div>
	);
};

export default ChangePasswordComponent;