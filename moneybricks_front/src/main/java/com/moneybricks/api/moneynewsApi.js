import axios from 'axios';

export const getMoneyNews = async (query, page = 1, size = 10) => {
    try {
        const response = await axios.get('/api/moneynews', {
            params: { query, page, size },
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching Money News:', error);
        throw error;
    }
};
