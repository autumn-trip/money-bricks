import React from 'react';
import "../style/NavComponent.scss";

export const NavComponent = () => {
    return (
        <div className="nav-container">
            <div className="main-layout-left">
                <div className="image1">
                    <img
                        src="/images/moneyBricks_main banner.png"
                        alt="Main Banner"
                        className="responsive-image"
                    />
                </div>
                <div className="news">
                        <h2>뉴스</h2>
                </div>

            </div>
            <div className="main-layout-right">
                <div className="dictionary">
                    <h2>사전</h2>
                </div>
                <div className="image2">
                    <img
                        src="/images/moneyBricks_quiz banner.png"
                        alt="Quiz Banner"
                        className="responsive-image"
                    />
                </div>
            </div>
        </div>
    );
};

export default NavComponent;