import axios from 'axios';

const API_SERVER_HOST = "http://localhost:8080";
const host = `${API_SERVER_HOST}/api/moneynews`;

export const getMoneyNews = async (query, page = 1, size = 10) => {
    try {
        const response = await axios.get(`${host}`, {
            params: { query, page, size },
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching Money News:', error);
        throw error;
    }
};