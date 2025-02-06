import jwtAxios from "../../common/util/jwtUtil";  // JWT 인증 포함된 axios 인스턴스
import { API_SERVER_HOST } from "./loginApi";  // API 서버 주소

const API_BASE_URL = `${API_SERVER_HOST}/api/points`;

export const getPointsInfo = async () => {
	try {
		const response = await jwtAxios.get(`${API_BASE_URL}/info`);
		return response.data; // PointsDTO 반환
	} catch (error) {
		console.error("포인트 정보를 불러오는 중 오류 발생:", error);
		throw error; // 에러를 호출한 곳으로 전달
	}
};
