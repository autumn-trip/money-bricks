import axios from "axios";
import jwtAxios from "../../common/util/jwtUtil";
import { API_SERVER_HOST } from "./loginApi";  // API 서버 주소

const host = `${API_SERVER_HOST}/api/member`;

// 회원가입 API 요청
export const registerMember = async (memberSignUpDTO) => {
	try {
		const response = await axios.post(`${host}/signup`, memberSignUpDTO);
		return response.data;
	} catch (error) {
		console.error("회원가입 실패:", error);
		throw error;
	}
};

// 회원 정보 조회
export const getMemberInfo = async () => {
	try {
		const response = await jwtAxios.get(`${host}/me`);
		return response.data;
	} catch (error) {
		console.error("회원 정보 조회 실패:", error);
		throw error;
	}
};

// 회원 정보 수정
export const updateMember = async (updateDTO) => {
	try {
		const response = await jwtAxios.put(`${host}/me`, updateDTO);
		return response.data;
	} catch (error) {
		console.error("회원 정보 수정 실패", error);
		throw error;
	}
};

// 비밀번호 변경
export const changePassword = async (passwordChangeDTO) => {
	try {
		const response = await jwtAxios.put(`${host}/me/password`, passwordChangeDTO);
		return response.data;
	} catch (error) {
		console.error("비밀번호 변경 실패", error);
		throw error;
	}
};

// 회원 탈퇴
export const deleteMember = async () => {
	try {
		const response = await jwtAxios.delete(`${host}/me`);
		return response.data;
	} catch (error) {
		console.error("회원 탈퇴 실패", error);
		throw error;
	}
};

// 중복 체크
export const checkDuplicate = async (field, value) => {
	try {
		const params = { [field]: value };
		const response = await axios.get(`${host}/check-duplicate`, { params });
		return {
			isDuplicate: !!response.data[field],
			message: response.data[field] || `사용 가능한 ${field}입니다.`
		};
	} catch (error) {
		console.error("중복 체크 요청 실패:", error);
		throw error;
	}
};

// 비밀번호 확인
export const checkPassword = async (passwordRequestDTO) => {
	try {
		const response = await jwtAxios.post(`${host}/verify-password`, passwordRequestDTO);
		return response.data; // 성공 시 응답 데이터를 반환
	} catch (error) {
		console.error("비밀번호 확인 실패", error);
		throw error;
	}
};

// 로그인 플래그 업데이트
export const updateFirstLoginFlag = async () => {
	try {
		const response = await jwtAxios.put(`${host}/updateFirstLoginFlag`);
		return response.data; // 성공 시 응답 데이터를 반환
	} catch (error) {
		console.error("로그인 플래그 업데이트 실패", error);
		throw error;
	}
}
