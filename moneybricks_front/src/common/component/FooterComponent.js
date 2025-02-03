import "../style/footerComponent.scss"
import React from "react";
import {Link} from "react-router-dom";

// logo 이미지
const logoSrc =
    `${process.env.PUBLIC_URL}/images/moneybricks_logo.png`

const footerComponent = () => {

    return (
    <footer className="footer-container">
        <div className="footer-bottom">
            {/* 상단 섹션 */}
            <div className="footer-content">
                {/* 왼쪽 컨택트 정보 */}
                <div className="footer-contact">
                    <h2>MoneyBricks는</h2>
                    <p>
                        금융을 이해하고 활용할 수 있도록 돕는 금융 교육 플랫폼, MoneyBricks. 쉽고 재미있는 금융 학습으로 여러분의 경제적 자신감을 키워드립니다.
                    </p>
                </div>
            </div>

            {/* 하단 섹션 */}
            <p>
                개인정보처리방침 | 사이트맵
                <br/>
                MoneyBricks | 대표자: 이중모 | 사업자등록번호:
                000-00-00000
                <br/>
                주소: 서울 서초구 서초대로77길 41 대동2빌딩 10층 3 강의실 | Tel. 010-3559-2192 | Fax.
                070-0000-0000 | E-mail. harun107@naver.com
            </p>
            <p>Copyright © MoneyBricks. All rights Reserved. Design by 4 team</p>
            <div className="footer-logo">
                <Link to="/">
                    <img src={logoSrc} alt="Logo" />
                </Link>
            </div>
        </div>
    </footer>
    )
};

export default footerComponent;