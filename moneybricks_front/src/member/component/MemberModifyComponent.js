import React, { useEffect, useState } from "react";
import LoadingSpinner from "../../common/component/LoadingSpinner";
import "../styles/MemberModifyComponent.scss";
import CustomModal from "../../common/component/CustomModal";
import {checkDuplicate, getMemberInfo, updateMember} from "../api/memberApi";

const initState = {
	name: "",
	nickname: "",
	phoneNumber: "",
	emailAgreed: false,  // 이메일 동의 여부
};

// 유효성 검사 규칙
const validationRules = {
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
};

const MemberModifyComponent = () => {
	const [isLoading, setIsLoading] = useState(false);  // 로딩 상태
	const [isFetching, setIsFetching] = useState(false);
	const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
	const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);
	const [originalNickname, setOriginalNickname] = useState(""); // 원래 닉네임 저장

	const [memberInfo, setMemberInfo] = useState(null);
	const [formData, setFormData] = useState(initState);
	const [errors, setErrors] = useState({});
	const [duplicateChecks, setDuplicateChecks] = useState({
		nickname: { checked: false, checking: false },
	});

	// 컴포넌트가 마운트될 때 사용자 데이터 로드
	useEffect(() => {
		const fetchMemberInfo = async () => {
			setIsLoading(true);
			try {
				const data = await getMemberInfo();
				setMemberInfo(data);
				setOriginalNickname(data.nickname || ""); // 원래 닉네임 저장
				// 수정 가능한 필드만 formData에 설정
				setFormData({
					name: data.name || "",
					nickname: data.nickname || "",
					phoneNumber: data.phoneNumber || "",
					emailAgreed: data.emailAgreed || false,
				});
				// 기존 닉네임인 경우 중복체크 통과 상태로 설정
				setDuplicateChecks({
					nickname: { checked: true, checking: false },
				});
			} catch (error) {
				console.error("회원 정보를 가져오는 데 실패했습니다.");
			} finally {
				setIsLoading(false);
			}
		};
		fetchMemberInfo();
	}, []);

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

		// 닉네임이 변경된 경우, 원래 닉네임과 다를 때만 중복체크 상태 초기화
		if (name === "nickname" && newValue !== originalNickname) {
			setDuplicateChecks(prev => ({
				...prev,
				nickname: { checked: false, checking: false },
			}));
		} else if (name === "nickname" && newValue === originalNickname) {
			// 원래 닉네임으로 돌아온 경우 중복체크 통과 상태로 설정
			setDuplicateChecks(prev => ({
				...prev,
				nickname: { checked: true, checking: false },
			}));
		}
	};


	// 중복 확인 핸들러
	const handleDuplicateCheck = async (field) => {
		const error = validateField(field, formData[field]);
		if (error) {
			alert(error);
			// 유효성 검사를 통과하지 못한 경우 중복 확인 진행하지 않음
			return;
		}

		// 현재 닉네임이 원래 닉네임과 같은 경우 중복 체크 불필요
		if (field === "nickname" && formData[field] === originalNickname) {
			setDuplicateChecks(prev => ({
				...prev,
				[field]: { checked: true, checking: false },
			}));
			alert("현재 사용 중인 닉네임입니다.");
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
			if (response.isDuplicate) {
				alert(`이미 존재하는 닉네임입니다.`);
			} else {
				setDuplicateChecks(prev => ({
					...prev,
					[field]: { checked: true, checking: false },
				}));
				alert(`${formData[field]}는 사용 가능한 닉네임입니다.`);
			}
		} catch (error) {
			console.error(error);
			alert("중복 확인 중 오류가 발생했습니다.");
		} finally {
			setIsFetching(false);
		}
	};

	// 회원 정보 수정 폼 제출 핸들러
	const handleSubmit = async (e) => {
		e.preventDefault();

		// 닉네임이 변경되었고 중복확인이 되지 않은 경우에만 체크
		if (formData.nickname !== originalNickname && !duplicateChecks.nickname.checked) {
			alert("닉네임 중복 확인이 필요합니다.");
			return;
		}

		setIsConfirmModalOpen(true);
	};

	// 실제 수정 처리
	const handleConfirmModify = async () => {
		setIsConfirmModalOpen(false);
		setIsLoading(true);

		try {
			const response = await updateMember(formData);
			console.log(response)
			setIsSuccessModalOpen(true);

		} catch (error) {
			console.error("회원 정보 수정 실패:", error);
			alert("회원 정보 수정 중 오류가 발생했습니다.");
		} finally {
			setIsLoading(false);
		}
	};

	// 수정 완료 후 처리
	const handleSuccessModalClose = () => {
		setIsSuccessModalOpen(false);
		window.location.reload();
	};

	if (!memberInfo) {
		return <LoadingSpinner />;
	}

	return (
		<>
			<div className="modify-container">
				{(isLoading || isFetching) && <LoadingSpinner />}
				<form onSubmit={handleSubmit} className="modify-form">
					<h1>회원 정보 수정</h1>
					<div className="modify-form-group">
						<label className="modify-form-label">아이디</label>
						<div className="read-only-field">{memberInfo.username}</div>
					</div>

					<div className="modify-form-group">
						<label className="modify-form-label">이메일</label>
						<div className="read-only-field">{memberInfo.email}</div>
					</div>

					<div className="modify-form-group">
						<label htmlFor="name" className="modify-form-label">
							이름<span className="modify-form-required">*</span>
						</label>
						<input
							type="text"
							name="name"
							id="name"
							value={formData.name}
							onChange={handleChange}
							className="modify-form-input"
						/>
						{errors.name && <p className="error-message">{errors.name}</p>}
					</div>

					<div className="modify-form-group">
						<label htmlFor="nickname" className="modify-form-label">
							닉네임<span className="modify-form-required">*</span>
						</label>
						<div className="input-with-button">
							<input
								type="text"
								name="nickname"
								id="nickname"
								value={formData.nickname}
								onChange={handleChange}
								className="modify-form-input"
							/>
							<button
								type="button"
								onClick={() => handleDuplicateCheck("nickname")}
								className="duplicate-check-btn"
							>
								중복 확인
							</button>
						</div>
						{errors.nickname && <p className="error-message">{errors.nickname}</p>}
					</div>

					<div className="modify-form-group">
						<label htmlFor="phoneNumber" className="modify-form-label">
							전화번호<span className="modify-form-required">*</span>
						</label>
						<input
							type="tel"
							name="phoneNumber"
							id="phoneNumber"
							maxLength="11"
							placeholder="숫자만 입력하세요. 예) 01000000000"
							value={formData.phoneNumber}
							onChange={handleChange}
							className="modify-form-input"
						/>
						{errors.phoneNumber && <p className="error-message">{errors.phoneNumber}</p>}
					</div>

					<div className="modify-form-group">
						<label className="modify-form-label">주민등록번호</label>
						<div className="read-only-field">
							{memberInfo.ssn ? `${memberInfo.ssn.substring(0, 6)}-${memberInfo.ssn.substring(6, 7)}******` : ""}
						</div>
					</div>

					<div className="modify-form-group">
						<label htmlFor="emailAgreed" className="modify-form-label checkbox-label">
							이메일 수신 동의 (선택)
							<input
								type="checkbox"
								name="emailAgreed"
								id="emailAgreed"
								checked={formData.emailAgreed}
								onChange={handleChange}
								className="modify-form-checkbox"
							/>
						</label>
					</div>

					<button type="submit" className="member-modify-btn">
						수정하기
					</button>
				</form>
			</div>

			{/* 확인 모달 */}
			<CustomModal
				isOpen={isConfirmModalOpen}
				onClose={() => setIsConfirmModalOpen(false)}
				title="회원 정보 수정"
				buttons={
					<>
						<button onClick={handleConfirmModify} className="confirm-btn">
							확인
						</button>
						<button onClick={() => setIsConfirmModalOpen(false)} className="cancel-btn">
							취소
						</button>
					</>
				}
			>
				<p>입력하신 정보로 수정하시겠습니까?</p>
			</CustomModal>

			{/* 성공 모달 */}
			<CustomModal
				isOpen={isSuccessModalOpen}
				onClose={handleSuccessModalClose}
				title="수정 완료"
				buttons={
					<button onClick={handleSuccessModalClose} className="confirm-btn">
						확인
					</button>
				}
			>
				<p>회원 정보가 성공적으로 수정되었습니다.</p>
			</CustomModal>
		</>
	);
};

export default MemberModifyComponent;
