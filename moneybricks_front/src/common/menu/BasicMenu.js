import React from "react";
import {Link} from "react-router-dom";
import "../style/BasicMenu.scss";
import useCustomLogin from "../hooks/useCustomLogin";

const BasicMenu = () => {
    const {isLogin, moveToLogin, doLogout, moveToPath} = useCustomLogin();

    const handleLogout = () => {
        doLogout();
        alert("로그아웃되었습니다.");
        moveToPath("/");
    };

    // 로고 이미지 선택
    const logoSrc = `${process.env.PUBLIC_URL}/images/moneybricks_logo.png` // 메인 페이지 투명 상태 로고

    return (
        <header className="menu-header">
            <link
                href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
                rel="stylesheet"
            />
            <div className="logo">
                <Link to="/">
                    <img src={logoSrc} alt="Logo"/>
                </Link>
            </div>
            <div className="icons">
                {isLogin ? (
                    <button className="menu-login" onClick={handleLogout}>
                        <span>LOGOUT</span>
                    </button>
                ) : (
                    <button className="menu-login" onClick={moveToLogin}>
                        <span>SIGN IN</span>
                    </button>
                )}
            </div>
        </header>
    );
};

export default BasicMenu;
