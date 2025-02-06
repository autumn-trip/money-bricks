import axios from "axios";

const API_SERVER_HOST = "http://localhost:8080";
const prefix = `${API_SERVER_HOST}/api/dictionary`;

export const getAllTerms = async (pageNumber = 0, pageSize = 50) => {
    try {
        const response = await axios.get(`${prefix}/all`, {
            headers: { 'Content-Type': 'application/json' },
            withCredentials: false,
            params: { page: pageNumber, size: pageSize }, // 페이지네이션 요청 추가
        });

        console.log("API 응답 데이터:", response.data); // 응답 데이터 확인
        return response.data; // 전체 응답 반환 (content, totalPages 등 포함)
    } catch (error) {
        console.error("Error fetching all terms:", error.response || error);
        throw error;
    }
};

// 코드(카테고리)별 용어 조회
export const getTermsByCode = async (categoryCode, pageNumber = 0, pageSize = 50) => {
    try {
        const response = await axios.get(`${prefix}/code`, {
            params: {
                code: categoryCode,
                page: pageNumber,
                size: pageSize},
            headers: { 'Content-Type': 'application/json' },
            withCredentials: false,
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching terms by category:", error.response || error);
        throw error;
    }
};

// 키워드 검색
export const searchTerms = async (keyword, pageNumber, code = null) => {
    const response = await axios.get(`${prefix}/search`, {
        params: { keyword, page: pageNumber, code }, // code 추가
    });
    return response.data;
};

// 메인 페이지
export const getRandomTerms = async () => {
    const response = await axios.get(`${prefix}/random`);
    return response.data;
}