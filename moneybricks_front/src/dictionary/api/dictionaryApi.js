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


//0123
// // 모든 용어 조회
// export const getAllTerms = async () => {
//     try {
//         const response = await axios.get(`${prefix}/all`, {
//             headers: { 'Content-Type': 'application/json' }, // 추가 헤더
//             withCredentials: false // 인증 정보 포함 여부
//         });
//         console.log("API 응답 데이터:", response.data); // 응답 데이터 확인
//         return response.data?.content || []; // content 필드 반환
//     } catch (error) {
//         console.error("Error fetching all terms:", error.response || error);
//         throw error;
//     }
// };

// export const getAllTerms = async () => {
//     try {
//         const response = await axios.get(`${prefix}/all`, {
//             headers: { 'Content-Type': 'application/json' }, // 추가 헤더
//             withCredentials: false, // 인증 정보 포함 여부
//         });
//         // return response.data;
//         return response.data?.data || [];
//     } catch (error) {
//         console.error("Error fetching all terms:", error.response || error);
//         throw error;
//
//     }
//
// };


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


//이전 코드
// export const searchTerms = async (keyword,code = null,  pageNumber = 0, pageSize = 50) => {
//     try {
//         const response = await axios.get(`${prefix}/search`, {
//             params: {
//                 keyword, code,
//                 page: pageNumber,
//                 size: pageSize,
//             },
//         });
//         return response.data;
//     } catch (error) {
//         console.error("Error searching terms:", error.response || error);
//         throw error;
//     }
// };

// export const searchTerms = async (keyword , pageNumber = 0, pageSize = 50) => {
//     try {
//         const response = await axios.get(`${prefix}/search`, {
//             params: {
//                 keyword,
//                 page:pageNumber,
//                 size: pageSize},
//             headers: { 'Content-Type': 'application/json' }, // 추가 헤더
//             withCredentials: false, // 인증 정보 포함 여부
//         });
//         return response.data;
//     } catch (error) {
//         console.error("Error searching terms:", error.response || error);
//         throw error;
//     }
// };

// import axios from "axios";
//
//
// const API_SERVER_HOST = "http://localhost:8080";
// const prefix = `${API_SERVER_HOST}/api/dictionary`;
//
// // 모든 용어 조회
// export const getAllTerms = async () => {
//     try {
//         const response = await axios.get(`${prefix}/all`);
//         return response.data;
//     } catch (error) {
//         console.error("Error fetching all terms:", error);
//         throw error;
//     }
// };
//
// // 코드(카테고리)별 용어 조회
// export const getTermsByCode = async (categoryCode) => {
//     try {
//         const response = await axios.get(`${prefix}/code`, {
//             params: { code: categoryCode },
//             headers: { 'Content-Type': 'application/json' }, // 추가 헤더 설정
//             withCredentials: false, // 인증 정보 포함 (선택 사항)
//         });
//         return response.data;
//     } catch (error) {
//         console.error("Error fetching terms by category:", error.response || error);
//         throw error;
//     }
// };
//
// // 키워드 검색
// export const searchTerms = async (keyword) => {
//     try {
//         const response = await axios.get(`${prefix}/search`, {
//             params: { keyword },
//         });
//         return response.data;
//     } catch (error) {
//         console.error("Error searching terms:", error);
//         throw error;
//     }
// };
//
//
