import jwtAxios from "../../common/util/jwtUtil";  // jwtAxios import
import { API_SERVER_HOST } from "./loginApi";  // API 서버 주소

// 계좌 조회 API
export const getSavingsAccount = async () => {
	try {
		const response = await jwtAxios.get(`${API_SERVER_HOST}/api/savings/account`);
		return response.data;
	} catch (error) {
		console.error("계좌 조회 중 오류가 발생했습니다:", error);
		throw error;
	}
};

// 만기 처리 API
export const checkMaturityAndProcess = async () => {
	try {
		const response = await jwtAxios.post(`${API_SERVER_HOST}/api/savings/check-maturity`);
		return response.data;
	} catch (error) {
		console.error("만기 처리 중 오류가 발생했습니다:", error);
		throw error;
	}
};

// 입금 API
export const deposit = async (depositAmount) => {
	try {
		const response = await jwtAxios.post(
			`${API_SERVER_HOST}/api/savings/deposit`,
			depositAmount
		);
		return response.data;
	} catch (error) {
		console.error("입금 처리 중 오류가 발생했습니다:", error);
		throw error;
	}
};

// 계좌 갱신 API
export const renewSavingsAccount = async (renewalPeriod) => {
	try {
		const response = await jwtAxios.post(`${API_SERVER_HOST}/api/savings/renew`, {
			params: { renewalPeriod }
		});
		return response.data;
	} catch (error) {
		console.error("계좌 갱신 중 오류가 발생했습니다:", error);
		throw error;
	}
};

// 중도 해지 API
export const cancelSavingsAccount = async () => {
	try {
		const response = await jwtAxios.post(`${API_SERVER_HOST}/api/savings/cancel`);
		return response.data;
	} catch (error) {
		console.error("계좌 중도 해지 중 오류가 발생했습니다:", error);
		throw error;
	}
};

// 입금 내역 조회 API
export const getDepositHistory = async () => {
	try {
		const response = await jwtAxios.get(`${API_SERVER_HOST}/api/deposits/history`);
		return response.data;
	} catch (error) {
		console.error("입금 내역 조회 중 오류가 발생했습니다:", error);
		throw error;
	}
};

// 입금 내역 (startDate 이후) 조회 API
export const getCurrentDepositHistory = async () => {
	try {
		const response = await jwtAxios.get(`${API_SERVER_HOST}/api/deposits/current-history`);
		return response.data;
	} catch (error) {
		console.error("현재 입금 내역 조회 중 오류가 발생했습니다:", error);
		throw error;
	}
};

// 계좌 이력 목록 조회 API
export const getAccountHistoryList = async () => {
	try {
		const response = await jwtAxios.get(`${API_SERVER_HOST}/api/account/history/list`);
		return response.data;
	} catch (error) {
		console.error("계좌 이력 목록 조회 중 오류가 발생했습니다:", error);
		throw error;
	}
};

// 계좌 이력 상세 조회 API
export const getAccountHistory = async (historyId) => {
	try {
		const response = await jwtAxios.get(`${API_SERVER_HOST}/api/account/history/${historyId}`);
		return response.data;
	} catch (error) {
		console.error("계좌 이력 상세 조회 중 오류가 발생했습니다:", error);
		throw error;
	}
};