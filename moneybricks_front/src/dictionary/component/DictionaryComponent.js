import React, { useState, useEffect } from "react";
import {getAllTerms, getTermsByCode, searchTerms} from "../api/dictionaryApi";
import AutoCompleteInput from "./AutoCompleteInput";


const DictionaryComponent = () => {
    const [terms, setTerms] = useState([]); // 전체 데이터
    const [filteredSuggestions, setFilteredSuggestions] = useState([]); // 추천 단어 데이터
    const [code, setCode] = useState(null); // 현재 카테고리 코드
    const [searchKeyword, setSearchKeyword] = useState(""); // 검색어
    const [isSearching, setIsSearching] = useState(false); // 검색 중 여부
    const [loading, setLoading] = useState(false); // 로딩 상태
    const [error, setError] = useState(""); // 에러 메시지
    const [activeButton, setActiveButton] = useState("all"); // 활성화된 버튼
    const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
    const [totalPages, setTotalPages] = useState(0); // 총 페이지 수

    // 데이터 가져오기
    const fetchData = async () => {
        if (loading) return; // 중복 요청 방지
        setLoading(true);
        setError("");

        try {
            let data;
            console.log("현재 검색어:", searchKeyword, "현재 코드:", code);
            if (isSearching ) {
                // 검색 요청에 code를 추가하여 검색 범위를 제한
                data = await searchTerms(searchKeyword, currentPage);
            } else if (code !== null) {
                data = await getTermsByCode(code, currentPage); // 카테고리 요청
            } else {
                data = await getAllTerms(currentPage); // 전체 데이터 요청
            }

            setTerms(data.content || []);
            setTotalPages(data.totalPages || 1);
        } catch (err) {
            setError("데이터를 가져오는 중 오류가 발생했습니다.");
        } finally {
            setLoading(false);
        }
    };


    // 데이터 로드
    useEffect(() => {
        fetchData();
    }, [code, currentPage, searchKeyword]); // isSearching 제거


    // 검색 핸들러
    const handleSearch = (keyword) => {
        if (searchKeyword !== keyword) {
            setSearchKeyword(keyword);
            setIsSearching(true);
            setCurrentPage(0);  // 검색어가 변경될 때만 초기화
            setCode(null);
        }
    };

    // 추천 단어 필터링
    const filterSuggestions = (keyword) => {
        if (!keyword.trim()) {
            setFilteredSuggestions([]);
            return;
        }
        const filtered = terms.filter((term) =>
            term.dictionaryTerms.toLowerCase().includes(keyword.toLowerCase())
        );
        setFilteredSuggestions(filtered);
    };

    // 버튼 클릭 핸들러
    const handleButtonClick = (categoryCode, buttonName) => {
        setCode(categoryCode);
        setCurrentPage(0);
        setIsSearching(false);
        setActiveButton(buttonName);
    };

    const isFirstPage = currentPage === 0;
    const isLastPage = currentPage === totalPages - 1;

    return (
        <div >
            <h1>Dictionary</h1>
            {/* 카테고리 버튼 */}
            <div >
                <button
                    onClick={() => handleButtonClick(null, "all")}
                    className={activeButton === "all" ? "active" : ""}                 >
                >

                    전체
                </button>
                <button
                    onClick={() => handleButtonClick(1, 1)}
                    className={activeButton === 1 ? "active" : ""}
                >
                    금융
                </button>
                <button
                    onClick={() => handleButtonClick(2, 2)}
                    className={activeButton === 2 ? "active" : ""}
                >
                    경제
                </button>
                <button
                    onClick={() => handleButtonClick(3, 3)}
                    className={activeButton === 3 ? "active" : ""}
                >
                    경영
                </button>
            </div>

            {/* 자동 완성 입력 필드 */}
            <div >
                <AutoCompleteInput
                    suggestions={filteredSuggestions}
                    onSearch={(keyword) => {
                        handleSearch(keyword);
                        filterSuggestions(keyword); // 추천 단어 필터링
                    }}
                />
            </div>


            {loading && <p>Loading...</p>}
            {error && <p style={{color: "red"}}>{error}</p>}

            {/* 데이터 리스트 */}
            <ul>
                {Array.isArray(terms) && terms.length > 0 ? (
                    terms.map((term) => (
                        <li key={term.dictionaryId}>
                            <strong>{term.dictionaryTerms}</strong>: {term.dictionaryDefinitions}
                        </li>
                    ))
                ) : (
                    !loading && <p>No terms available.</p>
                )}
            </ul>

            {/* 페이지네이션 버튼 */}
            <div className="pagination-container">
                <button onClick={() => setCurrentPage(0)} disabled={isFirstPage} className="pagination-button">
                    처음
                </button>
                <button
                    onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 0))}
                    disabled={isFirstPage}
                >
                    이전
                </button>
                <span>
          PAGE {currentPage + 1} of {totalPages}
        </span>
                <button
                    onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages - 1))}
                    disabled={isLastPage}
                >
                    다음
                </button>
                <button onClick={() => setCurrentPage(totalPages - 1)} disabled={isLastPage}>
                    마지막
                </button>
            </div>
        </div>
    );
};

export default DictionaryComponent;