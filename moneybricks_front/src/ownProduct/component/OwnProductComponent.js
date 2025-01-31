import "../style/OwnProductComponent.scss"
import React from "react";

const OwnProductComponent = () => {
    return (
        <div className="own-product-container">
            <div className="own-product">
                <div className="product-header">
                    <h1>머니브릭스 정기적금</h1>
                    <div className="interest-rates">
                        <div>
                            <span className="rate-label">최고</span>
                            <span className="rate-value">연 7.0%</span>
                        </div>
                        <div>
                            <span className="rate-label">기본</span>
                            <span className="rate-value">연 1.5%</span>
                        </div>
                    </div>
                </div>

                <div className="special-notice">
                    <p>개설일로부터 31회 모두 납입 완료하면 최고금리를 받아요.</p>
                </div>

                <div className="product-details">
                    <h2>상품 안내</h2>
                    <ul>
                        <li><strong>가입대상:</strong> 실명의 개인</li>
                        <li><strong>예금종류:</strong> 정기적금 (자유적립식)</li>
                        <li><strong>최초 가입금액:</strong> 0원으로만 가입 가능</li>
                        <li><strong>납입방법:</strong> 직접 납입을 통해서 1일 1회만 입금 가능하며, 그 외의 입금은 모두 제한</li>
                        <li><strong>1회 납입금액:</strong> 100원 이상 3만원 이하 (1원단위로 납입 가능)</li>
                        <li><strong>계약기간:</strong> 31일</li>
                        <li><strong>이자지급방식:</strong> 만기일시지급식</li>
                        <li><strong>이자계산방법:</strong> 입금한 건별로 예치기간만큼 약정된 금리를 적용 및 합산하여 이자 지급</li>
                        <li><strong>세금혜택:</strong> 비과세종합저축으로 가입 가능</li>
                        <li><strong>예금자보호대상:</strong> 원금과 소정의 이자를 합하여 1인당 "5천만원까지" 보호</li>
                    </ul>
                </div>

                <div className="important-notices">
                    <h3>유의 사항</h3>
                    <ul>
                        <li>30일 적금 완료 시 포인트 제공</li>
                        <li>적금 완료 후 재등록 가능</li>
                        <li>웹사이트 방문 후 적금 입금</li>
                    </ul>
                </div>
            </div>
        </div>
    );
};


export default OwnProductComponent;