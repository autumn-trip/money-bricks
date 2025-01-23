import React from "react";
import "../style/MainPage.scss";
import {Link} from "react-router-dom";
import FooterComponent from "../component/FooterComponent";


const SearchIcon = () => (
    <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" fill="none">
        <circle cx="11" cy="11" r="8" />
        <line x1="21" y1="21" x2="16.65" y2="16.65" />
    </svg>
);

// logo 이미지
const logoSrc =
    `${process.env.PUBLIC_URL}/images/moneybricks_logo.png`

const MainPage = () => {
    const categories = [
        { id: 1, title: '모의 주식' },
        { id: 2, title: '예적금 비교', path: "/product" },
        { id: 3, title: '커뮤니티' },
        { id: 4, title: '금융 정보' },
        { id: 5, title: '퀴즈' },
        { id: 6, title: '포인트 샵' },
    ];

    return (
        <div className="main-page">
            <header className="header">
                <div className="header-content">
                    <div className="logo">
                        <Link to="/">
                            <img src={logoSrc} alt="Logo" />
                        </Link>
                    </div>
                    <div className="header-right">
                        <button className="btn">마이페이지</button>
                        <button className="btn">고객센터</button>
                        <button className="btn">로그인</button>
                    </div>
                </div>
            </header>

            <div className="search-container">
                <div className="search-box">
                    <input type="text" placeholder="검색어를 입력해 주세요" />
                    <button className="search-button">
                        <SearchIcon />
                    </button>
                </div>
            </div>

            <nav className="category-menu">
                {categories.map((category) => (
                    <div key={category.id} className="category-item">
                        {category.title}
                    </div>
                ))}
            </nav>
            <div>
                <FooterComponent/>
            </div>
        </div>
    );
};


export default MainPage;