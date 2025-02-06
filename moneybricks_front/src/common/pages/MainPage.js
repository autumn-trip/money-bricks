import React, { useEffect, useState } from "react";
import "../style/MainPage.scss";
import { Link, useNavigate } from "react-router-dom";
import FooterComponent from "../component/FooterComponent";
import useCustomLogin from "../hooks/useCustomLogin";
import CustomModal from "../component/CustomModal";
import NavComponent from "../component/NavComponent";
import {checkMaturityAndProcess} from "../../member/api/accountApi";


const SearchIcon = () => (
    <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" fill="none">
        <circle cx="11" cy="11" r="8" />
        <line x1="21" y1="21" x2="16.65" y2="16.65" />
    </svg>
);

// logo 이미지
const logoSrc =
    `${process.env.PUBLIC_URL}/images/moneybricks_logo.png`;

const MainPage = () => {
    const categories = [
        { id: 1, title: "모의 주식" },
        { id: 2, title: "예적금 비교" },
        { id: 3, title: "커뮤니티" },
        { id: 4, title: "사전 / 뉴스" },
        { id: 5, title: "퀴즈" },
        { id: 6, title: "포인트 샵" },
    ];

    const { loginState, isLogin, moveToLogin, doLogout, moveToPath } = useCustomLogin();
    const navigate = useNavigate();
    const [isFirstLogin, setIsFirstLogin] = useState(false);
    const [isMatured, setIsMatured] = useState(false); // 만기 여부 상태

    useEffect(() => {
        // 첫 로그인 상태가 true일 경우 모달을 띄움
        if (loginState.firstLoginFlag) {
            setIsFirstLogin(true);
        }
    }, [loginState]);

    useEffect(() => {
        const checkMaturity = async () => {
            try {
                const result = await checkMaturityAndProcess(); // { isMatured: true/false }
                if (result.isMatured) { // 만기 처리되었으면 모달 띄우기
                    setIsMatured(true);
                }
            } catch (error) {
                console.error("만기 처리 중 오류가 발생했습니다.", error);
            }
        };

        checkMaturity();
    }, []); // 컴포넌트 마운트 시에만 실행

    const handleCloseModal = () => {
        setIsFirstLogin(false);
        setIsMatured(false); // 만기 모달도 닫기
    };

    const handleGoToAccount = () => {
        navigate("/account"); // 계좌 정보 페이지로 이동
        setIsFirstLogin(false); // 모달 닫기
    };

    const handleLogout = () => {
        doLogout();
        alert("로그아웃되었습니다.");
        moveToPath("/");
    };

    return (
        <div className="main-page">
            <header className="header">
                <div className="header-content">
                    <div className="logo">
                        <Link to="/">
                            <img src={logoSrc} alt="Logo"/>
                        </Link>
                    </div>
                    <div className="header-right">
                        <button className="btn">마이페이지</button>
                        <button className="btn">고객센터</button>
                        {isLogin ? (
                            <>
                                <button className="btn" onClick={handleLogout}>
                                    <span>로그아웃</span>
                                </button>
                                <Link to="/member/settings/edit-member" className="btn">
                                    설정
                                </Link>
                            </>
                        ) : (
                            <button className="btn" onClick={moveToLogin}>
                                <span>로그인</span>
                            </button>

                        )}
                    </div>
                </div>
            </header>

            <div className="search-container">
                <div className="search-box">
                    <input type="text" placeholder="검색어를 입력해 주세요"/>
                    <button className="search-button">
                        <SearchIcon/>
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
                <NavComponent/>
            </div>
            <div>
                <FooterComponent/>
            </div>

            {/* 첫 로그인일 때만 모달 띄우기 */}
            {isFirstLogin && (
                <CustomModal
                    isOpen={isFirstLogin}
                    onClose={handleCloseModal}
                    title="환영합니다!" buttons={
                    <button onClick={handleGoToAccount} className="confirm-btn">
                        확인
                    </button>
                }
                >
                    <div className="modal-body">첫 로그인을 환영합니다!<br/> 포인트 적금 계좌 정보를 확인하세요!</div>
                </CustomModal>
            )}

            {/* 만기 처리된 계좌일 때만 모달 띄우기 */}
            {isMatured && (
                <CustomModal
                    isOpen={isMatured}
                    onClose={handleCloseModal}
                    title="만기 처리"
                    buttons={
                        <button onClick={handleGoToAccount} className="confirm-btn">
                            계좌 갱신
                        </button>
                    }
                >
                    <div className="modal-body">
                        만기된 계좌가 있습니다. 계좌 갱신을 진행하시겠습니까?
                    </div>
                </CustomModal>
            )}
        </div>
    );
};


export default MainPage;