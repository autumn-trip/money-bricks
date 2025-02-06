import React from 'react';
import "../style/LoadingSpinner.scss"

const LoadingSpinner = ({isLoading, isFetching, children}) => {
    const loadingGif = `${process.env.PUBLIC_URL}/images/loading.gif`; // 공통 로딩 gif 경로

    if (isLoading || isFetching) {
        return (
            <div className="loading-container">
                <img src={loadingGif} alt="로딩 중" className="loading-spinner"/>
                <p>{isLoading ? "페이지 로딩 중..." : "데이터를 불러오는 중..."}</p>
            </div>
        );
    }
};
export default LoadingSpinner;