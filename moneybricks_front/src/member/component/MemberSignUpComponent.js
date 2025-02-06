import React, { useState } from "react";
import LoadingSpinner from "../../common/component/LoadingSpinner";
import "../styles/MemberSignUpComponent.scss";
import CustomModal from "../../common/component/CustomModal";
import useCustomLogin from "../../common/hooks/useCustomLogin";
import {checkDuplicate, registerMember} from "../api/memberApi";

const initState = {
	username: "",
	password: "",
	confirmPassword: "",
	email: "",
	name: "",
	nickname: "",
	phoneNumber: "",
	ssnPart1: "",
	ssnPart2: "",
	emailAgreed: false,  // 이메일 동의 여부
};

// 유효성 검사 규칙
const validationRules = {
	username: {
		pattern: /^[A-Za-z0-9]{6,20}$/,
		message: "아이디는 영문자와 숫자 조합 6-20자리로 입력해주세요.",
	},
	password: {
		pattern: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/,
		message: "비밀번호는 영문자, 숫자, 특수문자(@$!%*#?&) 조합 8자리 이상 입력해주세요.",
	},
	email: {
		pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
		message: "이메일을 올바른 형식으로 입력해주세요 (예: example@domain.com).",
	},
	name: {
		pattern: /^[가-힣]{2,10}$/,
		message: "이름은 한글 2-10자리로 입력해주세요.",
	},
	nickname: {
		pattern: /^[A-Za-z0-9가-힣]{2,10}$/,
		message: "닉네임은 한글, 영문, 숫자 조합으로 2-10자리로 입력해주세요.",
	},
	phoneNumber: {
		pattern: /^01[0-9]{8,9}$/,
		message: "휴대폰 번호를 올바른 형식으로 입력해주세요 (예: 01012345678).",
	},
	ssnPart1: {
		pattern: /^[0-9]{6}$/,
		message: "주민등록번호 앞자리(생년월일) 6자리를 입력해주세요 (예: 900101).",
	},
	ssnPart2: {
		pattern: /^[1-4]{1}$/,
		message: "주민등록번호 뒷자리 첫 번호(1~4)를 입력해주세요.",
	},
};

const SignUpComponent = () => {
	const [isLoading, setIsLoading] = useState(false);  // 로딩 상태
	const [isFetching, setIsFetching] = useState(false);
	const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);

	const [formData, setFormData] = useState(initState);
	const [errors, setErrors] = useState({});
	const [duplicateChecks, setDuplicateChecks] = useState({
		username: { checked: false, checking: false },
		email: { checked: false, checking: false },
		nickname: { checked: false, checking: false },
	});
	const incompleteFields = Object.entries(duplicateChecks).filter(
		([field, status]) =>
			["username", "email", "nickname"].includes(field) && !status.checked
	);

	const { moveToLogin } = useCustomLogin();

	// 실시간 유효성 검사
	const validateField = (name, value) => {
		if (!validationRules[name]) return "";
		if (!value) return "필수 입력 항목입니다";
		if (!validationRules[name].pattern.test(value)) {
			return validationRules[name].message;
		}
		return "";
	};

	// 폼 값 변경 핸들러
	const handleChange = (e) => {
		const { name, value, type, checked } = e.target;
		const newValue = type === "checkbox" ? checked : value;

		setFormData(prev => ({ ...prev, [name]: newValue }));

		// 실시간 유효성 검사
		if (validationRules[name]) {
			const error = validateField(name, newValue);
			setErrors(prev => ({ ...prev, [name]: error }));
		}

		// 값이 변경되면 해당 필드의 중복체크 상태를 초기화
		if (["username", "email", "nickname"].includes(name)) {
			setDuplicateChecks(prev => ({
				...prev,
				[name]: { checked: false, checking: false },
			}));
		}

		// 비밀번호 확인 실시간 유효성 검사
		if (name === "confirmPassword") {
			const passwordError = formData.password !== newValue
				? "비밀번호가 일치하지 않습니다"
				: "";
			setErrors(prev => ({ ...prev, confirmPassword: passwordError }));
		}
	};

	// 중복 확인 핸들러
	const handleDuplicateCheck = async (field) => {
		const error = validateField(field, formData[field]);
		if (error) {
			alert(error)
			// 유효성 검사를 통과하지 못한 경우 중복 확인 진행하지 않음
			return;
		}

		// 중복 확인 상태 업데이트
		setDuplicateChecks(prev => ({
			...prev,
			[field]: { ...prev[field], checking: true, checked: false },
		}));

		setIsFetching(true);

		try {
			const response = await checkDuplicate(field, formData[field]);

			setDuplicateChecks(prev => ({
				...prev,
				[field]: {
					checked: false,
					checking: false,
				},
			}));

			// 중복 확인 결과에 따라 알림 표시
			const fieldMessage = field === "username" ? "ID" : field === "email" ? "이메일" : "닉네임";

			if (response.isDuplicate) {
				alert(`이미 존재하는 ${fieldMessage}입니다.`);
			} else {
				setDuplicateChecks(prev => ({
					...prev,
					[field]: {
						checked: true,
						checking: false,
					},
				}));
				const userValue = formData[field];
				alert(`${userValue}는 사용 가능한 ${fieldMessage}입니다.`);
			}
		} catch (error) {
			setDuplicateChecks(prev => ({
				...prev,
				[field]: {
					checked: false,
					checking: false,
				},
			}));
			console.error(error);
			alert("중복 확인 중 오류가 발생했습니다.");
		} finally {
			setIsFetching(false);
		}
	};

	const validateForm = () => {
		const newErrors = {};

		Object.keys(validationRules).forEach(field => {
			const error = validateField(field, formData[field]);
			if (error) newErrors[field] = error;
		});

		// 비밀번호 확인 유효성 검사
		if (!formData.confirmPassword) {
			newErrors.confirmPassword = "필수 입력 항목입니다"; // 값이 없을 때
		} else if (formData.password !== formData.confirmPassword) {
			newErrors.confirmPassword = "비밀번호가 일치하지 않습니다"; // 비밀번호 불일치
		}

		setErrors(newErrors);
		return Object.keys(newErrors).length === 0;
	};


	// 회원가입 폼 제출 핸들러
	const handleSubmit = async (e) => {
		e.preventDefault();

		if (!validateForm()) {
			return;
		}

		if (incompleteFields.length > 0) {
			alert(
				`${incompleteFields.map(([field]) => field).join(", ")}의 중복 확인이 필요합니다.`
			);
			return;
		}

		setIsLoading(true); // 회원가입 요청 시 로딩 상태 활성화

		try {
			const mergedSSN = `${formData.ssnPart1}${formData.ssnPart2}`;
			const dataToSend = { ...formData, ssn: mergedSSN };
			const response = await registerMember(dataToSend);

			if (response.success) {
				setIsSuccessModalOpen(true); // 회원가입 성공 시 모달 표시
			}
		} catch (error) {
			console.error("회원가입 실패:", error);
			alert("회원가입 중 오류가 발생했습니다.");
		} finally {
			setIsLoading(false);
		}
	};

	const renderDuplicateCheckButton = (field) => (
		<button
			type="button"
			className={`signup-duplicate-check-btn ${duplicateChecks[field].checking ? "checking" : ""}`}
			onClick={() => handleDuplicateCheck(field)}
			disabled={duplicateChecks[field].checking || !formData[field]}
		>
			{duplicateChecks[field].checking ? (
				<>
					<span className="signup-spinner" />
					확인중...
				</>
			) : duplicateChecks[field].checked ? (
				"✓ 확인"
			) : (
				"중복확인"
			)}
		</button>
	);

	return (
		<div className="signup-container">
			{(isLoading || isFetching) && <LoadingSpinner />} {/* 로딩 상태일 때 스피너 표시 */}

			<form onSubmit={handleSubmit} className="sign-up-form">
				<h1 className="signup-title">회원가입</h1>
				<p className="signup-description">
					MONEY BRICKS의 회원이 되어 다양한 혜택을 경험해보세요!
				</p>

				{/* 아이디 입력 필드 */}
				<div className="signup-group">
					<label className="signup-label">
						아이디<span className="signup-required">*</span>
					</label>
					<div className="signup-input-container">
						<input
							type="text"
							name="username"
							value={formData.username}
							onChange={handleChange}
							placeholder="영문자와 숫자 조합 6-20자리"
							maxLength="20"
							className={`signup-input ${errors.username ? "is-invalid" : ""}`}
						/>
						{renderDuplicateCheckButton("username")}
					</div>
					{errors.username && <div className="signup-feedback">{errors.username}</div>}
				</div>

				{/* 비밀번호 입력 필드 */}
				<div className="signup-group">
					<label className="signup-label">
						비밀번호<span className="signup-required">*</span>
					</label>
					<input
						type="password"
						name="password"
						value={formData.password}
						onChange={handleChange}
						placeholder="영문자, 숫자, 특수문자 조합 8자리 이상"
						className={`signup-input ${errors.password ? "is-invalid" : ""}`}
					/>
					{errors.password && <div className="signup-feedback">{errors.password}</div>}
				</div>

				{/* 비밀번호 확인 필드 */}
				<div className="signup-group">
					<label className="signup-label">
						비밀번호 확인<span className="signup-required">*</span>
					</label>
					<input
						type="password"
						name="confirmPassword"
						value={formData.confirmPassword}
						onChange={handleChange}
						placeholder="비밀번호를 다시 입력해주세요"
						className={`signup-input ${errors.confirmPassword ? "is-invalid" : ""}`}
					/>
					{errors.confirmPassword && <div className="signup-feedback">{errors.confirmPassword}</div>}
				</div>

				{/* 이메일 입력 필드 */}
				<div className="signup-group">
					<label className="signup-label">
						이메일<span className="signup-required">*</span>
					</label>
					<div className="signup-input-container">
						<input
							type="email"
							name="email"
							value={formData.email}
							onChange={handleChange}
							placeholder="example@email.com"
							className={`signup-input ${errors.email ? "is-invalid" : ""}`}
						/>
						{renderDuplicateCheckButton("email")}
					</div>
					{errors.email && <div className="signup-feedback">{errors.email}</div>}
				</div>

				{/* 이름 입력 필드 */}
				<div className="signup-group">
					<label className="signup-label">
						이름<span className="signup-required">*</span>
					</label>
					<input
						type="text"
						name="name"
						value={formData.name}
						onChange={handleChange}
						placeholder="한글 2-10자리"
						maxLength="10"
						className={`signup-input ${errors.name ? "is-invalid" : ""}`}
					/>
					{errors.name && <div className="signup-feedback">{errors.name}</div>}
				</div>

				{/* 닉네임 입력 필드 */}
				<div className="signup-group">
					<label className="signup-label">
						닉네임<span className="signup-required">*</span>
					</label>
					<div className="signup-input-container">
						<input
							type="text"
							name="nickname"
							value={formData.nickname}
							onChange={handleChange}
							placeholder="한글, 영문, 숫자 2-10자리"
							maxLength="10"
							className={`signup-input ${errors.nickname ? "is-invalid" : ""}`}
						/>
						{renderDuplicateCheckButton("nickname")}
					</div>
					{errors.nickname && <div className="signup-feedback">{errors.nickname}</div>}
				</div>

				{/* 전화번호 입력 필드 */}
				<div className="signup-group">
					<label className="signup-label">
						전화번호<span className="signup-required">*</span>
					</label>
					<input
						type="tel"
						name="phoneNumber"
						value={formData.phoneNumber}
						onChange={handleChange}
						placeholder="숫자만 입력 (01012345678)"
						maxLength="11"
						className={`signup-input ${errors.phoneNumber ? "is-invalid" : ""}`}
					/>
					{errors.phoneNumber && <div className="signup-feedback">{errors.phoneNumber}</div>}
				</div>

				{/* 주민등록번호 입력 필드 */}
				<div className="signup-group">
					<label className="signup-label">
						주민등록번호<span className="signup-required">*</span>
					</label>
					<div className="signup-ssn-container">
						<input
							type="text"
							name="ssnPart1"
							value={formData.ssnPart1}
							onChange={handleChange}
							placeholder="생년월일 6자리"
							maxLength="6"
							className={`signup-input ${errors.ssnPart1 ? "is-invalid" : ""}`}
						/>
						<span className="signup-divider">-</span>
						<input
							type="text"
							name="ssnPart2"
							value={formData.ssnPart2}
							onChange={handleChange}
							maxLength="1"
							placeholder="주민등록번호 뒷번호 1자리"
							className={`signup-input ${errors.ssnPart2 ? "is-invalid" : ""}`}
						/>
						<span className="signup-hidden">******</span>
					</div>
					{(errors.ssnPart1 || errors.ssnPart2) && (
						<div className="signup-feedback">
							{errors.ssnPart1 || errors.ssnPart2}
						</div>
					)}
				</div>

				{/* 이메일 수신 동의 체크박스 */}
				<div className="signup-group email-agreement">
					<label className="signup-label">
						이메일 수신 동의 (선택)
					</label>
					<input
						type="checkbox"
						name="emailAgreed"
						checked={formData.emailAgreed}
						onChange={handleChange}
						className="signup-input-checkbox"
					/>
				</div>

				{/* 제출 버튼 */}
				<button
					type="submit"
					className="signup-submit-btn"
					disabled={isLoading}
				>
					{isLoading ? (
						<>
							<span className="signup-spinner" />
							가입중...
						</>
					) : (
						"회원가입"
					)}
				</button>

				{/* 회원가입 성공 모달 */}
				<CustomModal
					isOpen={isSuccessModalOpen}
					onClose={moveToLogin}
					title="회원가입 완료!"
					buttons={<button onClick={moveToLogin} className="modal-confirm-btn">로그인하기</button>}
				>
					<p>회원가입이 완료되었습니다.<br /> 10,000 포인트가 지급되었습니다.</p>
				</CustomModal>
			</form>
		</div>
	);
};


export default SignUpComponent;
