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
            if (isSearching && searchKeyword) {
                // 검색 요청에 code를 추가하여 검색 범위를 제한
                data = await searchTerms(searchKeyword, currentPage, code);
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
        setSearchKeyword(keyword);
        setIsSearching(true);
        setCurrentPage(0);
        // 기존 카테고리 코드가 유지되도록 설정
        if (!code) setCode(null); // code가 없는 경우 전체 검색
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

//0127 정상코드
// import React, { useState, useEffect } from "react";
// import { getAllTerms, getTermsByCode, searchTerms } from "../../api/dictionaryApi";
// import AutoCompleteInput from '../dictionary/AutoCompleteInput';
//
// const DictionaryComponent = () => {
//     const [terms, setTerms] = useState([]);
//     const [code, setCode] = useState(null);
//     const [searchKeyword, setSearchKeyword] = useState(""); // 검색어
//     const [isSearching, setIsSearching] = useState(false);
//     const [loading, setLoading] = useState(false);
//     const [error, setError] = useState("");
//     const [activeButton, setActiveButton] = useState("all"); // 활성화된 버튼
//     const [currentPage, setCurrentPage] = useState(0); // 현재 페이지
//     const [totalPages, setTotalPages] = useState(0); // 총 페이지 수
//
//
//
//     // 전체 용어 가져오기
//     const fetchAllTerms = async (pageNumber = 0, code = null) => {
//         setLoading(true);
//         setError("");
//         try {
//             let data;
//             if (code) {
//                 // 코드별 요청
//                 data = await getTermsByCode(code, pageNumber);
//             } else {
//                 // 전체 요청
//                 data = await getAllTerms(pageNumber);
//             }
//
//             setTerms(data.content); // 현재 페이지 데이터
//             setTotalPages(data.totalPages); // 총 페이지 수
//         } catch (err) {
//             setError("데이터를 가져오는 중 오류가 발생했습니다.");
//             console.error(err);
//         } finally {
//             setLoading(false);
//         }
//     };
//
//     // 코드별 용어 가져오기
//     const fetchTermsByCode = async (categoryCode, pageNumber = 0) => {
//         setLoading(true);
//         setError("");
//         setCode(categoryCode);
//
//         try {
//             const data = await getTermsByCode(categoryCode, pageNumber); // 페이지 번호 전달
//             setTerms(data.content);
//             setTotalPages(data.totalPages);
//             setActiveButton(categoryCode); // 활성화된 버튼 업데이트
//         } catch (err) {
//             setError("Error fetching terms by code.");
//         } finally {
//             setLoading(false);
//         }
//     };
//
//
//     // 키워드 검색
//     const handleSearch = async (keyword) => {
//         setLoading(true);
//         setError("");
//         setIsSearching(true)
//         setSearchKeyword(keyword);
//         setCode(null);
//         setCurrentPage(0)
//
//         try {
//
//             const data = await searchTerms(keyword, code);
//             setTerms(data.content || []);
//             setTotalPages(data.totalPages || 1);
//             setActiveButton(null); // 활성화된 버튼 해제
//         } catch (err) {
//             setError("사전 내에 존재하지 않는 단어 입니다.");
//         } finally {
//             setLoading(false);
//         }
//     };
//
//
//
//     // 컴포넌트 마운트 시 전체 용어 가져오기
//     useEffect(() => {
//         console.log("useEffect triggered");
//         console.log("Current Code:", code);
//         console.log("Is Searching:", isSearching);
//         console.log("Search Keyword:", searchKeyword);
//         console.log("Current Page:", currentPage);
//
//         if (loading) return; // Prevent duplicate requests during loading
//
//         const fetchData = async () => {
//             setLoading(true);
//             try {
//                 if (isSearching && searchKeyword) {
//                     await handleSearch(searchKeyword);
//                 } else if (code) {
//                     await fetchTermsByCode(code, currentPage);
//                 } else {
//                     await fetchAllTerms(currentPage);
//                 }
//             } catch (err) {
//                 setError("데이터를 가져오는 중 오류가 발생했습니다.");
//             } finally {
//                 setLoading(false);
//             }
//         };
//
//         fetchData();
//     }, [code, currentPage, isSearching, searchKeyword]);
//
//
//
//
//     return (
//         <div>
//             <h1>Dictionary</h1>
//             <div>
//                 <div>
//                     <button
//                         onClick={() => {
//                             setCode(null); // 전체 데이터로 설정
//                             setCurrentPage(0); // 페이지 초기화
//                             setIsSearching(false);
//                         }}
//                         className={activeButton === "all" ? "active" : ""}
//                     >
//                         전체
//                     </button>
//                     <button
//                         onClick={() => {
//                             setCode(1); // 금융 데이터로 설정
//                             setCurrentPage(0);
//                             setIsSearching(false);
//                         }}
//                         className={activeButton === 1 ? "active" : ""}
//                     >
//                         금융
//                     </button>
//                     <button
//                         onClick={() => {
//                             setCode(2); // 경제 데이터로 설정
//                             setCurrentPage(0);
//                             setIsSearching(false);
//                         }}
//                         className={activeButton === 2 ? "active" : ""}
//                     >
//                         경제
//                     </button>
//                     <button
//                         onClick={() => {
//                             setCode(3); // 경영 데이터로 설정
//                             setCurrentPage(0);
//                             setIsSearching(false);
//                         }}
//                         className={activeButton === 3 ? "active" : ""}
//                     >
//                         경영
//                     </button>
//
//
//                 </div>
//
//
//             </div>
//             <div>
//
//
//                 <input
//                     type="text"
//                     placeholder="Search terms..."
//                     onKeyDown={(e) => {
//                         if (e.key === "Enter") handleSearch(e.target.value);
//                     }}
//                 />
//             </div>
//             {loading && <p>Loading...</p>}
//             {error && <p style={{color: "red"}}>{error}</p>}
//             <ul>
//                 {Array.isArray(terms) && terms.length > 0 ? (
//                     terms.map((term) => (
//                         <li key={term.dictionaryId}>
//                             <strong>{term.dictionaryTerms}</strong>: {term.dictionaryDefinitions}
//                         </li>
//                     ))
//                 ) : (
//                     !loading && <p>No terms available.</p>
//                 )}
//             </ul>
//             <div>
//                 {/* 처음 버튼 */}
//                 <button
//                     onClick={() => setCurrentPage(0)} // 첫 페이지 이동
//                     disabled={currentPage === 0} // 첫 페이지일 경우 비활성화
//                 >
//                     처음
//                 </button>
//
//                 {/* 이전 버튼 */}
//                 <button
//                     onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 0))} // 이전 페이지 이동
//                     disabled={currentPage === 0} // 첫 페이지일 경우 비활성화
//                 >
//                     이전
//                 </button>
//
//                 {/* 현재 페이지 표시 */}
//                 <span>
//                     PAGE {currentPage + 1} of {totalPages}
//                 </span>
//
//                 {/* 다음 버튼 */}
//                 <button
//                     onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages - 1))} // 다음 페이지 이동
//                     disabled={currentPage === totalPages - 1} // 마지막 페이지일 경우 비활성화
//                 >
//                     다음
//                 </button>
//
//                 {/* 마지막 버튼 */}
//                 <button
//                     onClick={() => setCurrentPage(totalPages - 1)} // 마지막 페이지 이동
//                     disabled={currentPage === totalPages - 1} // 마지막 페이지일 경우 비활성화
//                 >
//                     마지막
//                 </button>
//
//             </div>
//         </div>
//     );
// };
//
// export default DictionaryComponent;
//
