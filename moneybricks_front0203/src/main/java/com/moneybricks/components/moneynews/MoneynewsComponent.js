import React, { useState, useEffect } from 'react';
import { getMoneyNews } from '../../api/moneynewsApi';

function MoneynewsComponent() {
    const [query, setQuery] = useState('');
    const [news, setNews] = useState([]);
    const [page, setPage] = useState(1);

    useEffect(() => {
        if (query) fetchNews();
    }, [query, page]);

    const fetchNews = async () => {
        const data = await getMoneyNews(query, page);
        setNews(data);
    };

    return (
        <div>
            <input
                type="text"
                placeholder="검색어를 입력하세요"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
            />
            <ul>
                {news.map((item, index) => (
                    <li key={index}>
                        <h3>{item.title}</h3>
                        <p>{item.description}</p>
                        <a href={item.originallink} target="_blank" rel="noopener noreferrer">
                            기사 보기
                        </a>
                    </li>
                ))}
            </ul>
            <button onClick={() => setPage(page - 1)} disabled={page === 1}>
                이전 페이지
            </button>
            <button onClick={() => setPage(page + 1)}>다음 페이지</button>
        </div>
    );
}

export default MoneynewsComponent;