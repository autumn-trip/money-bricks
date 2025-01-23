import axios from "axios";

const API_SERVER_HOST = "http://localhost:8080";
const host = `${API_SERVER_HOST}/api/product`;

// 리스트 조회
export const getList = async (pageParam) => {
    const { page, size } = pageParam;

    const res = await axios.get(`${host}/list`, {
        params: { page: page, size: size }
    });

    return res.data;
}